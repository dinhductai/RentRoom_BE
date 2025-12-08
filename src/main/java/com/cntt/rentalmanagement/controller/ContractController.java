package com.cntt.rentalmanagement.controller;

import com.cntt.rentalmanagement.services.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/contract")
@RequiredArgsConstructor
public class ContractController {
    private final ContractService contractService;

    @PostMapping
    @PreAuthorize("hasRole('RENTALER')")
    public ResponseEntity<?> addContract(@RequestParam String name,
            @RequestParam Long roomId,
            @RequestParam String nameOfRent,
            @RequestParam Long numOfPeople,
            @RequestParam String phone,
            @RequestParam String deadlineContract,
            @RequestParam List<MultipartFile> files) {
        return ResponseEntity
                .ok(contractService.addContract(name, roomId, nameOfRent, numOfPeople, phone, deadlineContract, files));
    }

    @PostMapping("/with-rental-code")
    @PreAuthorize("hasRole('RENTALER')")
    public ResponseEntity<?> addContractWithRentalCode(@RequestParam String name,
            @RequestParam Long roomId,
            @RequestParam String rentalCode,
            @RequestParam String deadlineContract,
            @RequestParam List<MultipartFile> files) {
        return ResponseEntity
                .ok(contractService.addContractWithRentalCode(name, roomId, rentalCode, deadlineContract, files));
    }

    @GetMapping
    @PreAuthorize("hasRole('RENTALER')")
    public ResponseEntity<?> getAllContract(@RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam Integer pageNo,
            @RequestParam Integer pageSize) {
        return ResponseEntity.ok(contractService.getAllContractOfRentaler(name, phone, pageNo, pageSize));
    }

    @GetMapping("/customer")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllContractForCustomer(
            @RequestParam Integer pageNo,
            @RequestParam Integer pageSize) {
        return ResponseEntity.ok(contractService.getAllContractOfCustomer(pageNo, pageSize));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('RENTALER')")
    public ResponseEntity<?> getContractById(@PathVariable Long id) {
        return ResponseEntity.ok(contractService.getContractById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RENTALER')")
    public ResponseEntity<?> updateContractInfo(@PathVariable Long id,
            @RequestParam String name,
            @RequestParam Long roomId,
            @RequestParam String nameOfRent,
            @RequestParam Long numOfPeople,
            @RequestParam String phone,
            @RequestParam String deadlineContract,
            @RequestParam List<MultipartFile> files) {
        return ResponseEntity.ok(contractService.editContractInfo(id, name, roomId, nameOfRent, numOfPeople, phone,
                deadlineContract, files));
    }
}
