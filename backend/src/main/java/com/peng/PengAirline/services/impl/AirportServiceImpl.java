package com.peng.PengAirline.services.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.peng.PengAirline.dtos.AirportCreateDTO;
import com.peng.PengAirline.dtos.AirportUpdateDTO;
import com.peng.PengAirline.dtos.Response;
import com.peng.PengAirline.entities.Airport;
import com.peng.PengAirline.enums.City;
import com.peng.PengAirline.enums.Country;
import com.peng.PengAirline.exceptions.BadRequestException;
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

        // 1. 获取 DTO 中的数据
        Country country = airportCreateDTO.getCountry();
        City city = airportCreateDTO.getCity();
        
        // 2. 核心业务逻辑：校验  城市必须属于该国家
        if (!city.getCountry().equals(country)){
            throw new BadRequestException("City does not belong to the country");
        }
        // 3. DTO 转换为 实体 (Entity)
        Airport airport = modelMapper.map(airportCreateDTO, Airport.class);
        // 4. 持久化到数据库
        airportRepo.save(airport);

        // 5. 返回标准化的成功响应
        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Airport Created Successfully")
                .build();
        
    }

@Override
public Response<?> updateAirport(Long id, AirportUpdateDTO airportUpdateDTO) {
    // 从数据库中找出“旧的”机场实体
    Airport existingAirport = airportRepo.findById(id)
            .orElseThrow(() -> new NotFoundException("Airport Not Found"));

    // 逐字段“选择性更新”
    if (airportUpdateDTO.getCity() != null) {
        if (!airportUpdateDTO.getCity().getCountry().equals(existingAirport.getCountry())) {
            throw new BadRequestException("City does not belong to the country");
        }
        existingAirport.setCity(airportUpdateDTO.getCity());
    }

    if (airportUpdateDTO.getName() != null) {
        existingAirport.setName(airportUpdateDTO.getName());
    }

    if (airportUpdateDTO.getIataCode() != null) {
        existingAirport.setIataCode(airportUpdateDTO.getIataCode());
    }

    if (airportUpdateDTO.getCountry() != null) {
        existingAirport.setCountry(airportUpdateDTO.getCountry());
    }

    airportRepo.save(existingAirport);

    return Response.builder()
            .statusCode(HttpStatus.OK.value())
            .message("Airport updated successfully")
            .build();
}


    @Override
    public Response<List<AirportCreateDTO>> getAllAirports() {

        // 1. 从数据库获取所有 "实体" 列表
        List<AirportCreateDTO> airports = airportRepo.findAll().stream()
                // 2. 使用 Java Stream API，将每个 "实体" 映射为 "DTO"
                .map(airport -> modelMapper.map(airport, AirportCreateDTO.class))
                // 3. 收集成一个新的 "DTO 列表"
                .toList();

        // 4. 返回包含数据列表的统一响应
        return Response.<List<AirportCreateDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(airports.isEmpty() ? "No Airport Found": "Airport Retrieved successfully")
                .data(airports)
                .build();
    }

    @Override
    public Response<AirportCreateDTO> getAirportById(Long id) {

        // 1. 查找实体，如果找不到就抛出异常
        Airport airport = airportRepo.findById(id)
                .orElseThrow(()-> new NotFoundException("Airport Not Found"));

        // 2. 将找到的 实体 映射为 DTO
        AirportCreateDTO airportCreateDTO = modelMapper.map(airport, AirportCreateDTO.class);

        // 3. 返回包含该 DTO 数据的统一响应
        return Response.<AirportCreateDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Airport Retrieved successfully")
                .data(airportCreateDTO)
                .build();
    }

}
