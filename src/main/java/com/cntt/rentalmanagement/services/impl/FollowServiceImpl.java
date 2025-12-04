package com.cntt.rentalmanagement.services.impl;

import com.cntt.rentalmanagement.domain.models.Follow;
import com.cntt.rentalmanagement.domain.models.User;
import com.cntt.rentalmanagement.domain.payload.request.FollowRequest;
import com.cntt.rentalmanagement.domain.payload.response.FollowResponse;
import com.cntt.rentalmanagement.domain.payload.response.MessageResponse;
import com.cntt.rentalmanagement.exception.BadRequestException;
import com.cntt.rentalmanagement.repository.FollowRepository;
import com.cntt.rentalmanagement.repository.UserRepository;
import com.cntt.rentalmanagement.services.BaseService;
import com.cntt.rentalmanagement.services.FollowService;
import com.cntt.rentalmanagement.utils.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl extends BaseService implements FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    private final MapperUtils mapperUtils;

    @Override
    public MessageResponse addFollow(FollowRequest followRequest) {
        User customer = userRepository.findById(getUserId()).orElseThrow(() -> new BadRequestException("Tài khoảng không tồn tại"));
        User rentaler = userRepository.findById(followRequest.getRentalerId()).orElseThrow(() -> new BadRequestException("Tài khoảng không tồn tại"));
        Optional<Follow> followOptional = followRepository.findByCustomerAndRentaler(customer, rentaler);
        if (followOptional.isPresent()) {
            throw new BadRequestException("Người cho thuê đã được theo dõi.");
        }
        Follow follow = new Follow();
        follow.setCustomer(customer);
        follow.setRentaler(rentaler);
        followRepository.save(follow);
        return MessageResponse.builder().message("Đã theo dõi.").build();
    }

    @Override
    public Page<FollowResponse> getAllFollowOfCustomer(Integer pageNo, Integer pageSize) {
        int page = pageNo == 0 ? pageNo : pageNo - 1;
        Pageable pageable = PageRequest.of(page, pageSize);
        return mapperUtils.convertToResponsePage(followRepository.getPageFollow(getUserId(),pageable),FollowResponse.class, pageable);
    }
}
