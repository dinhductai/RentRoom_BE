package com.cntt.rentalmanagement.domain.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowResponse {
    private UserResponse customer;
    private UserResponse rentaler;
}
