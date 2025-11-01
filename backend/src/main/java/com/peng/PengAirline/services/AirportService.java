package com.peng.PengAirline.services;

import java.util.List;

import com.peng.PengAirline.dtos.AirportCreateDTO;
import com.peng.PengAirline.dtos.AirportUpdateDTO;
import com.peng.PengAirline.dtos.Response;

public interface AirportService {

    Response<?> createAirport(AirportCreateDTO airportCreateDTO);

    Response<?> updateAirport(Long id, AirportUpdateDTO airportUpdateDTO);

    Response<List<AirportCreateDTO>> getAllAirports();

    Response<AirportCreateDTO> getAirportById(Long id);

}
