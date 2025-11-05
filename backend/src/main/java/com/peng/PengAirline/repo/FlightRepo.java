package com.peng.PengAirline.repo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.peng.PengAirline.entities.Flight;
import com.peng.PengAirline.enums.FlightStatus;

public interface FlightRepo extends JpaRepository<Flight, Long>{

    boolean existsByFlightNumber(String flightNumber);    

    List<Flight> findByDepartureAirport_IataCodeAndArrivalAirport_IataCodeAndStatusAndDepartureTimeBetween(
        String departIataCode, String arrivalIataCode, FlightStatus status, LocalDateTime startOfDay, LocalDateTime endOfDay
    );
    // ✅ 带 EntityGraph + 明确查询
    @EntityGraph(attributePaths = {
        "departureAirport",
        "arrivalAirport",
        "assignedPilot",
        "bookings"
    })
    @Query("SELECT f FROM Flight f WHERE f.id = :id")
    Optional<Flight> findByIdWithDetails(@Param("id") Long id);
    //✅ @EntityGraph 的作用：告诉 Spring Data JPA：当调用 findById(id) 时，同时执行 fetch join，
    //把 "departureAirport", "arrivalAirport", "assignedPilot", "bookings" 这些关联对象也加载出来。
}
