package com.cntt.rentalmanagement.controller;

import com.cntt.rentalmanagement.domain.models.ElectricAndWater;
import com.cntt.rentalmanagement.domain.payload.response.ElectricAndWaterResponse;
import com.cntt.rentalmanagement.domain.payload.response.RoomResponse;
import com.cntt.rentalmanagement.secruity.TokenProvider;
import com.cntt.rentalmanagement.services.ElectricAndWaterService;
import com.cntt.rentalmanagement.services.RoomService;
import com.cntt.rentalmanagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/electric-water")
public class ElectricAndWaterController {
    @Autowired
    private ElectricAndWaterService electricAndWaterService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<?> getAllElectric(@RequestParam int pageNo,
                                            @RequestParam(defaultValue = "10") int pageSize,
                                            @RequestHeader("Authorization") String token) {
        token = token.substring(7);
        Long userId = tokenProvider.getUserIdFromToken(token);
        List<RoomResponse> rooms = roomService.getRoomByUser(userService.getUserById(userId));
        List<ElectricAndWaterResponse> electricAndWatersList = new ArrayList<>();

        for (RoomResponse room : rooms) {
            List<ElectricAndWaterResponse> electrics = electricAndWaterService.getElectricByRoom(room.getId());
            electricAndWatersList.addAll(electrics);
        }

        // Sử dụng PageRequest để tạo Pageable
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        // Tạo danh sách phân trang từ electricAndWatersList
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), electricAndWatersList.size());
        List<ElectricAndWaterResponse> pagedElectrics = electricAndWatersList.subList(start, end);

        Page<ElectricAndWaterResponse> page = new PageImpl<>(pagedElectrics, pageable, electricAndWatersList.size());

        return ResponseEntity.ok(page);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createElectricAndWater(@RequestBody ElectricAndWater electricAndWater) {
        ElectricAndWater newElectricAndWater = electricAndWaterService.saveElectric(electricAndWater);
        return ResponseEntity.ok(newElectricAndWater);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateElectricAndWater(@RequestBody ElectricAndWater electricAndWater, @PathVariable Long id) {
        ElectricAndWater updatedElectricAndWater = electricAndWaterService.updateElectric(electricAndWater, id);
        return ResponseEntity.ok(updatedElectricAndWater);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getElectricAndWater(@PathVariable Long id) {
        ElectricAndWaterResponse electric = electricAndWaterService.getElectricAndWater(id);
        return ResponseEntity.ok(electric);
    }
}
