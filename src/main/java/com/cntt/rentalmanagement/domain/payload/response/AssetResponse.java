package com.cntt.rentalmanagement.domain.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssetResponse {
    private Long id;
    private String name;
    private Integer number;
}
