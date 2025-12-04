package com.cntt.rentalmanagement.controller;

import com.cntt.rentalmanagement.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationRepository locationRepository;

    @GetMapping("/all")
    public ResponseEntity<?> getAllLocations() {
        return ResponseEntity.ok(locationRepository.findAll());
    }
}
