package com.cntt.rentalmanagement.domain.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RentalCodeResponse {
    private String code;
    private String userName;
    private String userEmail;
    private String userPhone;
    private String userAddress;
    private Boolean isUsed;
    private LocalDateTime expiredAt;
    private LocalDateTime createdAt;
}
