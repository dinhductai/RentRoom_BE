package com.cntt.rentalmanagement.domain.payload.response;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RevenueResponse {
    private Integer month;
    private BigDecimal revenue;
    private BigDecimal waterCost;
    private BigDecimal publicElectricCost;
    private BigDecimal internetCost;
}
