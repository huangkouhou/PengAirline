package com.peng.PengAirline.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.peng.PengAirline.enums.FlightStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateFlightRequest {

    private Long id;

    private FlightStatus status;

    @NotBlank(message = "Flight Number cannot be blank")
    private String flightNumber;

    @NotBlank(message = "Departure airport IATA code cannot be blank")
    private String departureAirportIataCode;

    @NotBlank(message = "Arrival airport IATA code cannot be blank")
    private String arrivalAirportIataCode;

    @NotNull(message = "Departure time cannot be null")
    private LocalDateTime departureTime;

    @NotNull(message = "Arrival time cannot be null")
    private LocalDateTime arrivalTime;

    @NotNull(message = "Base price cannot be null")
    @Positive(message = "Base Price Must be Positive")
    private BigDecimal basePrice;

    private Long pilotId;

}


// 总结 Summary
// 注解 Annotation	    来源 Library/Spec	     作用 Purpose
// @Data	            Project Lombok	        自动生成Getters, Setters, toString等方法。 🤖
// @Builder	            Project Lombok	        自动实现建造者模式，方便地创建对象。 🧱
// @AllArgsConstructor	Project Lombok	        自动生成包含所有参数的构造函数。 📦
// @NoArgsConstructor	Project Lombok	        自动生成无参数的构造函数。 👐
// @NotBlank	        Jakarta Bean Validation	校验字符串不为空白。 ✅
// @NotNull	            Jakarta Bean Validation	校验对象不为null。 ✅
// @Positive	        Jakarta Bean Validation	校验数字为正数。 ✅