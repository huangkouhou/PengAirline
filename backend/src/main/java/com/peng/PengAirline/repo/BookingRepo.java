package com.peng.PengAirline.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.peng.PengAirline.entities.Booking;

public interface BookingRepo extends JpaRepository<Booking, Long>{

    List<Booking> findByUserIdOrderByIdDesc(Long userId);

}
