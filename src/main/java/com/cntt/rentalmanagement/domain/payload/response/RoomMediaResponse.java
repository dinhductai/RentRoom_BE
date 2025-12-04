package com.cntt.rentalmanagement.domain.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomMediaResponse {
    private Long id;
    private String files;
    private String mediaType;
}
