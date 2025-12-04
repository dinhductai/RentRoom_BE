package com.cntt.rentalmanagement.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElectricAndWater {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
}
