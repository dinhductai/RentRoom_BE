package com.cntt.rentalmanagement.domain.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CostResponse {
    private String name;
    private BigDecimal cost;
}
