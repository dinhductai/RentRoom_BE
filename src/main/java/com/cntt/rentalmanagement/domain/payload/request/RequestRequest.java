package com.cntt.rentalmanagement.domain.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestRequest {
    private String description;
    private String nameOfRent;
    private Long roomId;
    private String phone;
}
