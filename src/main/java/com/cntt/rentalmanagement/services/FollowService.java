package com.cntt.rentalmanagement.services;

import com.cntt.rentalmanagement.domain.payload.request.FollowRequest;
import com.cntt.rentalmanagement.domain.payload.response.FollowResponse;
import com.cntt.rentalmanagement.domain.payload.response.MessageResponse;
import org.springframework.data.domain.Page;

public interface FollowService {
    MessageResponse addFollow(FollowRequest followRequest);

    Page<FollowResponse> getAllFollowOfCustomer(Integer pageNo, Integer pageSize);
}
