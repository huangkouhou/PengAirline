package com.peng.PengAirline.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.peng.PengAirline.entities.Booking;

public interface BookingRepo extends JpaRepository<Booking, Long>{

    //一次 SQL 查询把 Booking 所有关联的数据全部查出来，避免 Lazy Loading + N+1 查询问题
    @Query("""
        SELECT DISTINCT b FROM Booking b
        LEFT JOIN FETCH b.passengers
        LEFT JOIN FETCH b.flight f 
        LEFT JOIN FETCH f.departureAirport
        LEFT JOIN FETCH f.arrivalAirport        
            """)
    
    List<Booking> findAllWithPassengers();

    List<Booking> findByUserIdOrderByIdDesc(Long userId);

}
