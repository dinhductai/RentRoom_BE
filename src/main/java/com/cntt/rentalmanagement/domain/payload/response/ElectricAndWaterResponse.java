package com.cntt.rentalmanagement.domain.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ElectricAndWaterResponse {
    private Long id;
    private String name;
    private int month;
    private int lastMonthNumberOfElectric;
    private int thisMonthNumberOfElectric;
    private int lastMonthBlockOfWater;
    private int thisMonthBlockOfWater;
    private BigDecimal moneyEachNumberOfElectric;
    private BigDecimal moneyEachBlockOfWater;
    private BigDecimal totalMoneyOfElectric;
    private BigDecimal totalMoneyOfWater;
    private boolean paid;
    private RoomResponse room;
}
