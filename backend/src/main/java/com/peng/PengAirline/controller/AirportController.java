package com.peng.PengAirline.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.peng.PengAirline.dtos.AirportCreateDTO;
import com.peng.PengAirline.dtos.AirportDTO;
import com.peng.PengAirline.dtos.Response;
import com.peng.PengAirline.services.AirportService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@RequestMapping("/api/airports")
@RequiredArgsConstructor
public class AirportController {

    private final AirportService airportService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response<?>> createAirport(@Valid @RequestBody AirportCreateDTO airportCreateDTO) {
        return ResponseEntity.ok(airportService.createAirport(airportCreateDTO));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response<?>> updateAirport(
            @PathVariable Long id,
            @Valid @RequestBody AirportDTO airportDTO) {
        return ResponseEntity.ok(airportService.updateAirport(id, airportDTO));
    }


    @GetMapping // 不带参数的 GET → 获取集合资源 GET 方法只是读取，不会改变系统状态，因此不需要加 @PreAuthorize
    public ResponseEntity<Response<List<AirportDTO>>> getAllAirports() {
        return ResponseEntity.ok(airportService.getAllAirports());
    }

    @GetMapping("/{id}") // 带路径参数（/{id}） 的 GET → 获取单个资源
    public ResponseEntity<Response<AirportDTO>> getAirportById(@PathVariable Long id) {
        return ResponseEntity.ok(airportService.getAirportById(id));
    }

}
