package com.cntt.rentalmanagement.controller;

import com.cntt.rentalmanagement.domain.payload.request.BlogStoreRequest;
import com.cntt.rentalmanagement.services.BlogStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BlogStoreController {
    private final BlogStoreService blogStoreService;

    @PostMapping("/blog-store/save")
    public ResponseEntity<?> saveBlog(@RequestBody BlogStoreRequest storeRequest){
        return ResponseEntity.ok(blogStoreService.saveBlog(storeRequest));
    }

    @GetMapping("/blog-store/all")
    public ResponseEntity<?> getAllBlog(@RequestParam Integer pageNo,
                                        @RequestParam Integer pageSize) {
        return ResponseEntity.ok(blogStoreService.getPageOfBlog(pageNo, pageSize));
    }
}
