package com.peng.PengAirline.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.peng.PengAirline.dtos.CreateFlightRequest;
import com.peng.PengAirline.dtos.FlightDTO;
import com.peng.PengAirline.dtos.Response;
import com.peng.PengAirline.enums.City;
import com.peng.PengAirline.enums.Country;
import com.peng.PengAirline.enums.FlightStatus;
import com.peng.PengAirline.services.FlightService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor//Lombok 为 final 字段自动注入构造器（依赖注入）。
public class FlightController {

    private final FlightService flightService;

    //创建航班
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PILOT')")
    public ResponseEntity<Response<?>> createFlight(@Valid @RequestBody CreateFlightRequest createFlightRequest){
        return ResponseEntity.ok(flightService.createFlight(createFlightRequest));
    }

    //按 ID 查询航班
    @GetMapping("/{id}")
    public ResponseEntity<Response<FlightDTO>> getFlightById(@PathVariable Long id){
        return ResponseEntity.ok(flightService.getFlightById(id));
    }

    //查询全部航班
    @GetMapping
    public ResponseEntity<Response<List<FlightDTO>>> getAllFlights(){
        return ResponseEntity.ok(flightService.getAllFlights());
    }
    
    //更新航班（局部更新）
    @PutMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PILOT')")
    public ResponseEntity<Response<?>> updateFlight(@RequestBody CreateFlightRequest flightRequest){
        return ResponseEntity.ok(flightService.updateFlight(flightRequest));
    }

    //搜索航班
    @GetMapping("/search")
    public ResponseEntity<Response<List<FlightDTO>>> searchFlight(
            @RequestParam(required = true) String departureIataCode,
            @RequestParam(required = true) String arrivalIataCode,
            @RequestParam(required = false, defaultValue = "SCHEDULED") FlightStatus status,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate
    ){
        return ResponseEntity.ok(flightService.searchFlight(departureIataCode, arrivalIataCode, status, departureDate));
    }

    //获取全部城市
    @GetMapping("/cities")
    public ResponseEntity<Response<List<City>>> getAllCities(){
        return ResponseEntity.ok(flightService.getAllCities());
    }

    //获取全部国家
    @GetMapping("/countries")
    public ResponseEntity<Response<List<Country>>> getAllCountries(){
        return ResponseEntity.ok(flightService.getAllCountries());
    }
}
