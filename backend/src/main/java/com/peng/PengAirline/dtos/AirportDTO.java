package com.peng.PengAirline.dtos;

import com.peng.PengAirline.enums.City;
import com.peng.PengAirline.enums.Country;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirportDTO {

    private Long id;

    private String name;

    private City city;

    private Country country;

    private String iataCode;

}
