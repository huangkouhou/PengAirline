package com.peng.PengAirline.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.peng.PengAirline.enums.FlightStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightDTO {

    private Long id;

    private String flightNumber;

    private FlightStatus status;

    private AirportDTO departureAirport;

    private AirportDTO arrivalAirport;

    private LocalDateTime departureTime;

    private LocalDateTime arrivalTime;
    
    private BigDecimal basePrice;

    private UserDTO assignedPilot;

    private List<BookingDTO> bookings;

    //从 Flight 关联的 Airport 实体中提取的字段，用于简化前端交互，而非直接映射数据库。
    private String departAirportIataCode;
    private String arrivalAirportIataCode;

}
