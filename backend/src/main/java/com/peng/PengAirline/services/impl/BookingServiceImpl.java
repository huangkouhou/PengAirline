package com.peng.PengAirline.services.impl;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.peng.PengAirline.dtos.BookingDTO;
import com.peng.PengAirline.dtos.CreateBookingRequest;
import com.peng.PengAirline.dtos.Response;
import com.peng.PengAirline.entities.User;
import com.peng.PengAirline.enums.BookingStatus;
import com.peng.PengAirline.enums.FlightStatus;
import com.peng.PengAirline.exceptions.NotFoundException;
import com.peng.PengAirline.exceptions.BadRequestException;
import com.peng.PengAirline.entities.Booking;
import com.peng.PengAirline.entities.Flight;
import com.peng.PengAirline.entities.Passenger;
import com.peng.PengAirline.repo.BookingRepo;
import com.peng.PengAirline.repo.FlightRepo;
import com.peng.PengAirline.repo.PassengerRepo;
import com.peng.PengAirline.services.BookingService;
import com.peng.PengAirline.services.EmailNotificationService;
import com.peng.PengAirline.services.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{

    private final BookingRepo bookingRepo;
    private final UserService userService;
    private final FlightRepo flightRepo;
    private final PassengerRepo passengerRepo;
    private final ModelMapper modelMapper;//用于 DTO 和 Entity 之间转换的“转换器”。
    private final EmailNotificationService emailNotificationService;

    @Override
    @Transactional
    public Response<?> createBooking(CreateBookingRequest createBookingRequest){

        // 1. 获取当前操作的用户
        User user = userService.currentUser();

        // 2. 验证航班是否存在且有效
        Flight flight = flightRepo.findById(createBookingRequest.getFlightId())
                .orElseThrow(()-> new NotFoundException("Flight Not Found"));

        // 3. 核心业务逻辑校验
        if (flight.getStatus() != FlightStatus.SCHEDULED){
            throw new BadRequestException("You can only book a flight that is scheduled");
        }
        //强校验 —— 不允许 0 passenger
        if (createBookingRequest.getPassengers() == null
        || createBookingRequest.getPassengers().isEmpty()) {
        throw new BadRequestException("Booking must have at least one passenger");
        }

        // 4. 创建并设置 Booking (预订) 主体
        Booking booking = new Booking();
        booking.setBookingReference(generateBookingReference());
        booking.setUser(user);
        booking.setFlight(flight);
        booking.setBookingDate(LocalDateTime.now());
        booking.setStatus(BookingStatus.CONFIRMED);

        // 5. 保存 Booking 主体（这是第一次数据库操作）
        Booking savedBooking = bookingRepo.save(booking);

        // 6. 处理同行乘客（Passenger）
        if (createBookingRequest.getPassengers() != null && !createBookingRequest.getPassengers().isEmpty()){

            // 6a. 将 乘客DTO 列表 转换为 乘客实体 列表
            List<Passenger> passengers = createBookingRequest.getPassengers().stream()
                    .map(passengerDTO -> {
                        Passenger passenger = modelMapper.map(passengerDTO, Passenger.class);
                        passenger.setBooking(savedBooking);
                        return passenger;
                    }).toList();

            // 6c. 批量保存所有乘客（这是第二次数据库操作）
            passengerRepo.saveAll(passengers);
            savedBooking.setPassengers(passengers);// 更新 booking 对象，以便邮件服务使用
        }

        // 7. 触发外部服务：发送确认邮件
        emailNotificationService.sendBookingTickerEmail(savedBooking);

        // 8. 返回统一的成功响应
        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Booking created successfully")
                .build();
        
    }

    @Override
    public Response<BookingDTO> getBookingById(Long id){

        Booking booking = bookingRepo.findById(id)
                .orElseThrow(()-> new NotFoundException("Booking not found"));

        BookingDTO bookingDTO = modelMapper.map(booking, BookingDTO.class);
        bookingDTO.getFlight().setBookings(null);

        return Response.<BookingDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Booking Retrieved Successfully")
                .data(bookingDTO)
                .build();

    }

    @Override
    public Response<List<BookingDTO>> getAllBookings(){

        //Booking -> Passenger 的关系默认:@OneToMany(fetch = FetchType.LAZY)
        //意味着：从 DB 查询 booking 时 不会级联加载 passengers 表里的数据
        List<Booking> allBookings = bookingRepo.findAllWithPassengers();

        List<BookingDTO> bookings = allBookings.stream()
                    .map(booking -> {
                        BookingDTO bookingDTO = modelMapper.map(booking, BookingDTO.class);
                        bookingDTO.getFlight().setBookings(null);
                        return bookingDTO;
                    }).toList();
    
        return Response.<List<BookingDTO>>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message(bookings.isEmpty()? "No Booking Found": "Booking Retrieved Successfully")
                    .data(bookings)
                    .build();

    }

    @Override
    public Response<List<BookingDTO>> getMyBookings(){

        User user = userService.currentUser();
        List<Booking> userBookings = bookingRepo.findByUserIdOrderByIdDesc(user.getId());

        List<BookingDTO> bookings = userBookings.stream()
                .map(booking -> {
                    BookingDTO bookingDTO = modelMapper.map(booking, BookingDTO.class);
                    bookingDTO.getFlight().setBookings(null);
                    return bookingDTO;
                }).toList();
        
        return Response.<List<BookingDTO>>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message(bookings.isEmpty()? "No Booking Found for this user": "User Bookings retrieved successfully")
                    .data(bookings)
                    .build();

    }


    @Override
    @Transactional//把一系列数据库操作放在一个“事务”里，要么全部成功，要么全部回滚。
    public Response<?> updateBookingStatus(Long id, BookingStatus status){

        Booking booking = bookingRepo.findById(id)
                .orElseThrow(()-> new NotFoundException("Booking Not Found"));

        booking.setStatus(status);
        bookingRepo.save(booking);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Booking Updated Successfully")
                .build();
    }

    //生成业务单号：bookingReference 用 UUID.substring(0,8) 生成简短可读的参考号。
    private String generateBookingReference(){
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }


}
