package com.peng.PengAirline.services;

import java.time.LocalDate;
import java.util.List;

import com.peng.PengAirline.dtos.CreateFlightRequest;
import com.peng.PengAirline.dtos.FlightDTO;
import com.peng.PengAirline.dtos.Response;
import com.peng.PengAirline.enums.City;
import com.peng.PengAirline.enums.Country;
import com.peng.PengAirline.enums.FlightStatus;

public interface FlightService {

    Response<?> createFlight(CreateFlightRequest CreateFlightRequest);
    Response<FlightDTO> getFlightById(Long Id);
    Response<List<FlightDTO>> getAllFlights();
    Response<?> updateFlight(CreateFlightRequest createFlightRequest);
    Response<List<FlightDTO>> searchFlight(String departureIata, String arrivalPortIata, FlightStatus status, LocalDate departureDate);
    Response<List<City>> getAllCities();
    Response<List<Country>> getAllCountries();
}
