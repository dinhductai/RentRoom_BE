package com.cntt.rentalmanagement.domain.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TotalNumberResponse {
    private Integer numberOfAccount;
    private Integer numberOfApprove;
    private Integer numberOfApproving;
    private Integer numberOfAccountLocked;
}
