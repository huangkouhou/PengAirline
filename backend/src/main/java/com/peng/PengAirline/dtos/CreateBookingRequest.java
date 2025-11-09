package com.peng.PengAirline.dtos;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingRequest {

    @NotNull(message = "Flight ID cannot be null")
    private Long flightId;

    @NotEmpty(message = "At least one passenger must be provided")
    @Valid   // ✅ 让 Spring 校验每一个 PassengerDTO 对象
    private List<PassengerDTO> passengers;
    
}
