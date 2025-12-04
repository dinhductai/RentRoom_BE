package com.cntt.rentalmanagement.controller;

import com.cntt.rentalmanagement.services.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    private ResponseEntity<?> addContract(@RequestParam String name,
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
    private ResponseEntity<?> addContractWithRentalCode(@RequestParam String name,
            @RequestParam Long roomId,
            @RequestParam String rentalCode,
            @RequestParam String deadlineContract,
            @RequestParam List<MultipartFile> files) {
        return ResponseEntity
                .ok(contractService.addContractWithRentalCode(name, roomId, rentalCode, deadlineContract, files));
    }

    @GetMapping
    private ResponseEntity<?> getAllContract(@RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam Integer pageNo,
            @RequestParam Integer pageSize) {
        return ResponseEntity.ok(contractService.getAllContractOfRentaler(name, phone, pageNo, pageSize));
    }

    @GetMapping("/customer")
    private ResponseEntity<?> getAllContractForCustomer(
            @RequestParam(required = false) String phone,
            @RequestParam Integer pageNo,
            @RequestParam Integer pageSize) {
        return ResponseEntity.ok(contractService.getAllContractOfCustomer(phone, pageNo, pageSize));
    }

    @GetMapping("/{id}")
    private ResponseEntity<?> getContractById(@PathVariable Long id) {
        return ResponseEntity.ok(contractService.getContractById(id));
    }

    @PutMapping("/{id}")
    private ResponseEntity<?> updateContractInfo(@PathVariable Long id,
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
