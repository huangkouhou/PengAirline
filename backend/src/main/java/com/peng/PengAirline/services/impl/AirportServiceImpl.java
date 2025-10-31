package com.peng.PengAirline.services.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.peng.PengAirline.dtos.AirportDTO;
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
    
    private final AirportRepo airportRepo;
    private final ModelMapper modelMapper;
    
    
    @Override
    //创建机场
    public Response<?> createAirport(AirportDTO airportDTO) {
        log.info("Inside createAirport()");

        Country country = airportDTO.getCountry();
        City city = airportDTO.getCity();
        
        // 业务校验：城市必须属于该国家
        if (!city.getCountry().equals(country)){
            throw new BadRequestException("City does not belong to the country");
        }
        //  DTO -> Entity
        Airport airport = modelMapper.map(airportDTO, Airport.class);
        // 入库
        airportRepo.save(airport);

        // 统一响应
        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Airport Created Successfully")
                .build();
        
    }

    @Override
    public Response<?> updateAirport(AirportDTO airportDTO) {

        Long id = airportDTO.getId();

        // 先查旧数据
        Airport existingAirport = airportRepo.findById(id)
                .orElseThrow(()-> new NotFoundException("Airport Not Found"));

        // 逐字段“选择性更新”
        if (airportDTO.getCity() != null){
            if (!airportDTO.getCity().getCountry().equals(existingAirport.getCountry())){
                throw new BadRequestException("CITY does not belong to the country");
            }
            existingAirport.setCity(airportDTO.getCity());
        }

        if (airportDTO.getName() != null){
            existingAirport.setName(airportDTO.getName());
        }

        if (airportDTO.getIataCode() != null){
            existingAirport.setIataCode(airportDTO.getIataCode());
        }

        airportRepo.save(existingAirport);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Airport updated Successfully")
                .build();
    }

    @Override
    public Response<List<AirportDTO>> getAllAirports() {

        List<AirportDTO> airports = airportRepo.findAll().stream()
                .map(airport -> modelMapper.map(airport, AirportDTO.class))
                .toList();
        
        return Response.<List<AirportDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(airports.isEmpty() ? "No Airport Found": "Airport Retrieved successfully")
                .data(airports)
                .build();
    }

    @Override
    public Response<AirportDTO> getAirportById(Long id) {

        Airport airport = airportRepo.findById(id)
                .orElseThrow(()-> new NotFoundException("Airport Not Found"));

        AirportDTO airportDTO = modelMapper.map(airport, AirportDTO.class);

        return Response.<AirportDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Airport Retrieved successfully")
                .data(airportDTO)
                .build();
    }

}
