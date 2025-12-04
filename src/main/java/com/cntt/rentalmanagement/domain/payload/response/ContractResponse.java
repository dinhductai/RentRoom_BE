package com.cntt.rentalmanagement.domain.payload.response;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ContractResponse {
    private Long id;
    private String name;
    private String files;
    private String nameOfRent;
    private LocalDateTime deadlineContract;
    private RoomResponse room;
    private LocalDateTime createdAt;
    private String phone;
    private Long numOfPeople;

}
