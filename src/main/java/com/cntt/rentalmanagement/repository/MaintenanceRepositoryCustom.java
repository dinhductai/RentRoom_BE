package com.cntt.rentalmanagement.repository;

import com.cntt.rentalmanagement.domain.models.Maintenance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface MaintenanceRepositoryCustom {
    Page<Maintenance> searchingMaintenance(String keyword,Long userId, Pageable pageable);

    BigDecimal sumPriceOfMaintenance(Long userId);
}
