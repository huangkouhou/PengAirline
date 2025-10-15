package com.peng.PengAirline.dtos;

import com.peng.PengAirline.enums.PassengerType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassengerDTO {

    private Long id;

    @NotBlank(message = "First Name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last Name cannot be blank")
    private String lastName;
    private String passportNumber;

    @NotNull(message = "Passenger Type cannot be blank")
    private PassengerType type;

    private String seatNumber;

    private String specialRequest;
}
