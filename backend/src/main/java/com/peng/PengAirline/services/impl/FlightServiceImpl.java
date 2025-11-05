package com.peng.PengAirline.services.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.peng.PengAirline.dtos.CreateFlightRequest;
import com.peng.PengAirline.dtos.FlightDTO;
import com.peng.PengAirline.dtos.Response;
import com.peng.PengAirline.entities.Airport;
import com.peng.PengAirline.entities.Flight;
import com.peng.PengAirline.entities.User;
import com.peng.PengAirline.enums.FlightStatus;
import com.peng.PengAirline.enums.City;
import com.peng.PengAirline.enums.Country;
import com.peng.PengAirline.exceptions.NotFoundException;
import com.peng.PengAirline.repo.AirportRepo;
import com.peng.PengAirline.repo.FlightRepo;
import com.peng.PengAirline.repo.UserRepo;
import com.peng.PengAirline.services.FlightService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.peng.PengAirline.exceptions.BadRequestException;

@Service
@Slf4j
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightRepo flightRepo;
    private final AirportRepo airportRepo;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;

    @Override
    public Response<?> createFlight(CreateFlightRequest createFlightRequest) {

        //时间校验
        if (createFlightRequest.getArrivalTime().isBefore(createFlightRequest.getDepartureTime())) {
            throw new BadRequestException("Arrival Time cannot be before the Departure Time");
        }

        //航班号唯一性校验
        if (flightRepo.existsByFlightNumber(createFlightRequest.getFlightNumber())) {
            throw new BadRequestException("Flight with this number already exists");
        }

        //关联机场有效性校验
        Airport departureAirport = airportRepo.findByIataCode(createFlightRequest.getDepartureAirportIataCode())
                .orElseThrow(() -> new NotFoundException("Departure Airport Not Found"));

        Airport arrivalAirport = airportRepo.findByIataCode(createFlightRequest.getArrivalAirportIataCode())
                .orElseThrow(() -> new NotFoundException("Arrival Airport Not Found"));

        //组装并初始化实体
        Flight flightToSave = new Flight();
        flightToSave.setFlightNumber(createFlightRequest.getFlightNumber());
        flightToSave.setDepartureAirport(departureAirport);
        flightToSave.setArrivalAirport(arrivalAirport);
        flightToSave.setBasePrice(createFlightRequest.getBasePrice());
        flightToSave.setStatus(FlightStatus.SCHEDULED);

        //分配机长（飞行员）
        if (createFlightRequest.getId() != null) {
            User pilot = userRepo.findById(createFlightRequest.getId())
                    .orElseThrow(() -> new NotFoundException("Pilot is not found"));

            boolean isPilot = pilot.getRoles().stream()
                    .anyMatch(role -> role.getName().equalsIgnoreCase("PILOT"));

            if (!isPilot) {
                throw new BadRequestException("Claimed User-Pilot not a certified pilot");
            }

            flightToSave.setAssignedPilot(pilot);
        }

        flightRepo.save(flightToSave);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Flight saved successfully")
                .build();
    }

    @Override
    public Response<FlightDTO> getFlightById(Long id) {
        Flight flight = flightRepo.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Flight Not Found"));

        FlightDTO flightDTO = modelMapper.map(flight, FlightDTO.class);

        //断开循环引用  手动把每个 BookingDTO 里的 flight 属性设为 null，这样 Jackson 在序列化时不会再陷入死循环。
        if (flightDTO.getBookings() != null) {
            flightDTO.getBookings().forEach(bookingDTO -> bookingDTO.setFlight(null));
        }

        return Response.<FlightDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Flight Retrieved Successfully")
                .data(flightDTO)
                .build();
    }

    @Override
    public Response<List<FlightDTO>> getAllFlights() {
        Sort sortByIdDesc = Sort.by(Sort.Direction.DESC, "id");

        List<FlightDTO> flights = flightRepo.findAll(sortByIdDesc).stream()
                .map(flight -> {
                    FlightDTO flightDTO = modelMapper.map(flight, FlightDTO.class);
                    if (flightDTO.getBookings() != null) {
                        flightDTO.getBookings().forEach(bookingDTO -> bookingDTO.setFlight(null));
                    }
                    return flightDTO;
                }).toList();

        return Response.<List<FlightDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(flights.isEmpty() ? "No Flights Found" : "Flights Retrieved Successfully")
                .data(flights)
                .build();
    }

    @Override
    public Response<?> updateFlight(CreateFlightRequest flightRequest) {
        Long id = flightRequest.getId();
        Flight existingFlight = flightRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Flight Not Found"));

        if (flightRequest.getDepartureTime() != null) {
            existingFlight.setDepartureTime(flightRequest.getDepartureTime());
        }

        if (flightRequest.getArrivalTime() != null) {
            existingFlight.setArrivalTime(flightRequest.getArrivalTime());
        }

        if (flightRequest.getBasePrice() != null) {
            existingFlight.setBasePrice(flightRequest.getBasePrice());
        }

        if (flightRequest.getStatus() != null) {
            existingFlight.setStatus(flightRequest.getStatus());
        }

        if (flightRequest.getPilotId() != null) {
            User pilot = userRepo.findById(flightRequest.getPilotId())
                    .orElseThrow(() -> new NotFoundException("Pilot is not found"));

            boolean isPilot = pilot.getRoles().stream()
                    .anyMatch(role -> role.getName().equalsIgnoreCase("PILOT"));

            if (!isPilot) {
                throw new BadRequestException("Claimed User-Pilot not a certified pilot");
            }

            existingFlight.setAssignedPilot(pilot);
        }

        flightRepo.save(existingFlight);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Flight Updated Successfully")
                .build();
    }

    @Override
    public Response<List<FlightDTO>> searchFlight(String departurePortIata, String arrivalPortIata,
                                                  FlightStatus status, LocalDate departureDate) {

        //把 LocalDate 转成当天时间范围
        LocalDateTime startOfDay = departureDate.atStartOfDay();
        LocalDateTime endOfDay = departureDate.plusDays(1).atStartOfDay().minusNanos(1);

        //Repository 查询方法 这里的下划线 _ 是 Spring Data JPA 的关联属性导航写法（departureAirport.iataCode）。确保实体里字段名是 departureAirport、arrivalAirport，且它们有 iataCode 字段。
        List<Flight> flights = flightRepo.findByDepartureAirport_IataCodeAndArrivalAirport_IataCodeAndStatusAndDepartureTimeBetween(
                departurePortIata, arrivalPortIata, status, startOfDay, endOfDay);

        List<FlightDTO> flightDTOS = flights.stream()
                .map(flight -> {
                    FlightDTO flightDTO = modelMapper.map(flight, FlightDTO.class);
                    flightDTO.setAssignedPilot(null);
                    flightDTO.setBookings(null);
                    return flightDTO;
                }).toList();

        return Response.<List<FlightDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(flightDTOS.isEmpty() ? "No Flight Found" : "Flights Retrieved Successfully")
                .data(flightDTOS)
                .build();
    }

    @Override
    public Response<List<City>> getAllCities() {
        return Response.<List<City>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success")
                .data(List.of(City.values()))
                .build();
    }

    @Override
    public Response<List<Country>> getAllCountries() {
        return Response.<List<Country>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success")
                .data(List.of(Country.values()))
                .build();
    }

}
