package com.peng.PengAirline.services;

import java.util.List;

import com.peng.PengAirline.dtos.AirportCreateDTO;
import com.peng.PengAirline.dtos.AirportDTO;
import com.peng.PengAirline.dtos.Response;

public interface AirportService {

    Response<?> createAirport(AirportCreateDTO airportCreateDTO);

    Response<?> updateAirport(Long id, AirportDTO airportDTO);

    Response<List<AirportDTO>> getAllAirports();

    Response<AirportDTO> getAirportById(Long id);

}
