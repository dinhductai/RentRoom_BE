package com.cntt.rentalmanagement.services;

import com.cntt.rentalmanagement.domain.payload.response.RoomResponse;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

public interface BlogService {
    Page<RoomResponse> getAllRoomForAdmin(String title, Boolean approve, Integer pageNo, Integer pageSize);

    Page<RoomResponse> getAllRoomForCustomer(String title, BigDecimal price,Long category, Integer pageNo, Integer pageSize);
}
