package com.cntt.rentalmanagement.controller;

import com.cntt.rentalmanagement.domain.payload.request.FollowRequest;
import com.cntt.rentalmanagement.services.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping
    public ResponseEntity<?> followAgents(@RequestBody FollowRequest followRequest){
        return ResponseEntity.ok(followService.addFollow(followRequest));
    }

    @GetMapping
    public ResponseEntity<?> getAllAgents(@RequestParam Integer pageNo,
                                          @RequestParam Integer pageSize) {
        return ResponseEntity.ok(followService.getAllFollowOfCustomer(pageNo, pageSize));
    }
}
