package com.cntt.rentalmanagement.services;

import com.cntt.rentalmanagement.domain.payload.request.RequestRequest;
import com.cntt.rentalmanagement.domain.payload.response.MessageResponse;
import com.cntt.rentalmanagement.domain.payload.response.RequireResponse;
import org.springframework.data.domain.Page;

public interface RequestService {
    Page<RequireResponse> getRequestOfRentHome(String keyword, Integer pageNo, Integer pageSize);

    MessageResponse changeStatusOfRequest(Long id);

    RequireResponse getRequest(Long id);

    Page<RequireResponse> getRequestOfCustomer(String keyword, String phone, Integer pageNo, Integer pageSize);

    MessageResponse addRequest(RequestRequest request);
}
