package com.cntt.rentalmanagement.domain.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private Long userId;
    private String email;
    private String name;
    private List<String> roles;

    public AuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public AuthResponse(String accessToken, Long userId, String email, String name, List<String> roles) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.roles = roles;
    }
}
