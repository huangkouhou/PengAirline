package com.peng.PengAirline.dtos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.peng.PengAirline.entities.Flight;
import com.peng.PengAirline.enums.BookingStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class BookingDTO {

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    private Long id;

    private String bookingReference;

    private UserDTO user;

    private FlightDTO flight;

    private LocalDateTime bookingDate;

    private BookingStatus status;

    private List<PassengerDTO> passengers;

}
