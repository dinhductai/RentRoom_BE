package com.cntt.rentalmanagement.repository;

import com.cntt.rentalmanagement.domain.models.Follow;
import com.cntt.rentalmanagement.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> , FollowRepositoryCustom{
    Optional<Follow> findByCustomerAndRentaler(User customer, User rentaler);

}
