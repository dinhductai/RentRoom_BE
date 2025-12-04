package com.cntt.rentalmanagement.domain.payload.response;

import com.cntt.rentalmanagement.domain.models.Role;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserResponse {

    private Long id;

    private String name;

    private String email;

    private Boolean isLocked;

    private String address;

    private String phone;

    private String imageUrl;

    private String zaloUrl;

    private String facebookUrl;
}
