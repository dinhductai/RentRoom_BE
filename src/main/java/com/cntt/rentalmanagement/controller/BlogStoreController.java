package com.cntt.rentalmanagement.controller;

import com.cntt.rentalmanagement.domain.payload.request.BlogStoreRequest;
import com.cntt.rentalmanagement.services.BlogStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BlogStoreController {
    private final BlogStoreService blogStoreService;

    @PostMapping("/blog-store/save")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> saveBlog(@RequestBody BlogStoreRequest storeRequest){
        return ResponseEntity.ok(blogStoreService.saveBlog(storeRequest));
    }

    @GetMapping("/blog-store/all")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllBlog(@RequestParam Integer pageNo,
                                        @RequestParam Integer pageSize) {
        return ResponseEntity.ok(blogStoreService.getPageOfBlog(pageNo, pageSize));
    }
    
    @DeleteMapping("/blog-store/{roomId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> unsaveBlog(@PathVariable Long roomId) {
        return ResponseEntity.ok(blogStoreService.removeBlogStore(roomId));
    }
}
