package com.cntt.rentalmanagement.controller;

import com.cntt.rentalmanagement.services.RentalCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rental-code")
@RequiredArgsConstructor
public class RentalCodeController {

    private final RentalCodeService rentalCodeService;

    @PostMapping("/generate")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> generateRentalCode() {
        return ResponseEntity.ok(rentalCodeService.generateRentalCode());
    }

    @GetMapping("/current")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getCurrentUserRentalCode() {
        return ResponseEntity.ok(rentalCodeService.getCurrentUserRentalCode());
    }

    @GetMapping("/verify/{code}")
    @PreAuthorize("hasRole('RENTALER')")
    public ResponseEntity<?> verifyRentalCode(@PathVariable String code) {
        return ResponseEntity.ok(rentalCodeService.getRentalCodeByCode(code));
    }
}
