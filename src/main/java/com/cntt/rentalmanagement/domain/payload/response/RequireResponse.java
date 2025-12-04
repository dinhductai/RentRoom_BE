package com.cntt.rentalmanagement.domain.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequireResponse {
    private Long id;
    private String name;
    private String phoneNumber;
    private String description;
    private Boolean isAnswer;
    private RoomResponse room;
}
