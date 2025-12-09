package com.cntt.rentalmanagement.repository;

import com.cntt.rentalmanagement.domain.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepositoryCustom {
    Page<User> searchingAccount(String keyword, Pageable pageable);

    void deleteRoleOfAccount(Long id);

    Page<User> findRentalers(String name, Pageable pageable);

    List<User> findByNameContainingIgnoreCase(String userName);
}
