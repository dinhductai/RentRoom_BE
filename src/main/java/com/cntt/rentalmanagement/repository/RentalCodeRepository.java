package com.cntt.rentalmanagement.repository;

import com.cntt.rentalmanagement.domain.models.RentalCode;
import com.cntt.rentalmanagement.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RentalCodeRepository extends JpaRepository<RentalCode, Long> {
    Optional<RentalCode> findByCode(String code);

    Optional<RentalCode> findByUserAndIsUsedFalse(User user);
}
