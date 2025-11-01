package com.peng.PengAirline.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.peng.PengAirline.dtos.AirportDTO;
import com.peng.PengAirline.dtos.Response;
import com.peng.PengAirline.services.AirportService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;




@RestController
@RequestMapping("/api/airports")
@RequiredArgsConstructor
public class AirportController {

    private final AirportService airportService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response<?>>createAirport(@Valid@RequestBody AirportDTO airportDTO) {
        return ResponseEntity.ok(airportService.createAirport(airportDTO));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response<?>> updateAirport(@Valid@RequestBody AirportDTO airportDTO) {
        return ResponseEntity.ok(airportService.updateAirport(airportDTO));
    }

    @GetMapping
    public ResponseEntity<Response<List<AirportDTO>>> getAllAirports() {
        return ResponseEntity.ok(airportService.getAllAirports());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<AirportDTO>> getAirportById(@PathVariable Long id) {
        return ResponseEntity.ok(airportService.getAirportById(id));
    }
    


}
