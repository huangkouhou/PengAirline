package com.peng.PengAirline.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.peng.PengAirline.entities.Passenger;

public interface PassengerRepo extends JpaRepository<Passenger, Long>{

    
}
