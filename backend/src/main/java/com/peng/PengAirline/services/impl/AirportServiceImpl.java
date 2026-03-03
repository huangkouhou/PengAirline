package com.peng.PengAirline.services.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.peng.PengAirline.dtos.AirportCreateDTO;
import com.peng.PengAirline.dtos.AirportDTO;
import com.peng.PengAirline.dtos.Response;
import com.peng.PengAirline.entities.Airport;
import com.peng.PengAirline.exceptions.NotFoundException;
import com.peng.PengAirline.repo.AirportRepo;
import com.peng.PengAirline.services.AirportService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AirportServiceImpl implements AirportService{
    
    //Spring Boot在启动时，会自动将AirportRepo和ModelMapper的实例（Bean）“注入”到这个构造方法中。这就是依赖注入（Dependency Injection）。
    private final AirportRepo airportRepo;
    private final ModelMapper modelMapper;
    
    
    @Override
    //创建机场
    public Response<?> createAirport(AirportCreateDTO airportCreateDTO) {
        log.info("Inside createAirport()");

        // city/country 现在是 String，直接映射保存即可
        Airport airport = modelMapper.map(airportCreateDTO, Airport.class);
        airportRepo.save(airport);
        
        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Airport Created Successfully")
                .build();
    }

    @Override
    public Response<?> updateAirport(Long id, AirportDTO airportDTO) {
        // 从数据库中找出“旧的”机场实体
        Airport existingAirport = airportRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Airport Not Found"));

        //  String 字段：选择性更新
        if (airportDTO.getCity() != null) {
            existingAirport.setCity(airportDTO.getCity());
        }

        if (airportDTO.getName() != null) {
            existingAirport.setName(airportDTO.getName());
        }

        if (airportDTO.getIataCode() != null) {
            existingAirport.setIataCode(airportDTO.getIataCode());
        }

        if (airportDTO.getCountry() != null) {
            existingAirport.setCountry(airportDTO.getCountry());
        }

        airportRepo.save(existingAirport);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Airport updated successfully")
                .build();
    }


    @Override
    public Response<List<AirportDTO>> getAllAirports() {

        // 1. 从数据库获取所有 "实体" 列表
        List<AirportDTO> airports = airportRepo.findAll().stream()
                // 2. 使用 Java Stream API，将每个 "实体" 映射为 "DTO"
                .map(airport -> modelMapper.map(airport, AirportDTO.class))
                // 3. 收集成一个新的 "DTO 列表"
                .toList();

        // 4. 返回包含数据列表的统一响应
        return Response.<List<AirportDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(airports.isEmpty() ? "No Airport Found": "Airport Retrieved successfully")
                .data(airports)
                .build();
    }

    @Override
    public Response<AirportDTO> getAirportById(Long id) {

        // 1. 查找实体，如果找不到就抛出异常
        Airport airport = airportRepo.findById(id)
                .orElseThrow(()-> new NotFoundException("Airport Not Found"));

        // 2. 将找到的 实体 映射为 DTO
        AirportDTO airportDTO = modelMapper.map(airport, AirportDTO.class);

        // 3. 返回包含该 DTO 数据的统一响应
        return Response.<AirportDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Airport Retrieved successfully")
                .data(airportDTO)
                .build();
    }

}
