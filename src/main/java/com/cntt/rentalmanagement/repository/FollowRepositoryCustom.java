package com.cntt.rentalmanagement.repository;

import com.cntt.rentalmanagement.domain.models.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FollowRepositoryCustom {
    Page<Follow> getPageFollow(Long userId, Pageable pageable);
}
