package com.peng.PengAirline.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.peng.PengAirline.entities.Airport;

public interface AirportRepo extends JpaRepository<Airport, Long>{

    Optional<Airport> findByIataCode(String iataCode);

}
