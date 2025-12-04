package com.cntt.rentalmanagement.controller;

import com.cntt.rentalmanagement.domain.payload.request.ChatRequest;
import com.cntt.rentalmanagement.domain.payload.response.ChatResponse;
import com.cntt.rentalmanagement.services.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:8080" }, allowCredentials = "false")
public class AIController {

    private final AIService aiService;

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        System.out.println("=== AI Chat Request Received ===");
        System.out.println("Message: " + request.getMessage());

        try {
            String response = aiService.chatWithAI(request.getMessage());
            System.out.println("AI Response: " + response);
            return ResponseEntity.ok(new ChatResponse(response));
        } catch (Exception e) {
            System.err.println("Error in chat controller: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(new ChatResponse("Xin lỗi, đã xảy ra lỗi khi xử lý yêu cầu của bạn."));
        }
    }
}
