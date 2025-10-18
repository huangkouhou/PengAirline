package com.peng.PengAirline.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.peng.PengAirline.entities.Flight;
import com.peng.PengAirline.enums.FlightStatus;

public interface FlightRepo extends JpaRepository<Flight, Long>{

    boolean existsByFlightNumber(String flightNumber);    

    List<Flight> findByDepartureAirportIataCodeAndArrivalAirportIataCodeAndStatusAndDepartTimeBetween(
        String departIataCode, String arrivalIataCode, FlightStatus status, LocalDateTime startOfDay, LocalDateTime endOfDay
    );
}
