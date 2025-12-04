package com.cntt.rentalmanagement.domain.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TotalNumberRequest {
    private Integer numberOfRoom;
    private Integer numberOfPeople;
    private Integer numberOfEmptyRoom;
    private BigDecimal revenue;
}
