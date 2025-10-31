package com.peng.PengAirline.services;

import java.util.List;

import com.peng.PengAirline.dtos.AirportDTO;
import com.peng.PengAirline.dtos.Response;

public interface AirportService {

    Response<?> createAirport(AirportDTO airportDTO);

    Response<?> updateAirport(AirportDTO airportDTO);

    Response<List<AirportDTO>> getAllAirports();

    Response<AirportDTO> getAirportById(Long id);
}
