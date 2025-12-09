package com.cntt.rentalmanagement.domain.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageChatRequest {
    
    @NotBlank(message = "Nội dung tin nhắn không được để trống")
    private String content;
}
