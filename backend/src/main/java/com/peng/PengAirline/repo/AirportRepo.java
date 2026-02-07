package com.peng.PengAirline.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.peng.PengAirline.entities.Airport;

public interface AirportRepo extends JpaRepository<Airport, Long>{

    Optional<Airport> findByIataCode(String iataCode);

    @Query("SELECT DISTINCT a.city FROM Airport a ORDER BY a.city")
    List<String> findDistinctCities();

    @Query("SELECT DISTINCT a.country FROM Airport a ORDER BY a.country")
    List<String> findDistinctCountries();


}
