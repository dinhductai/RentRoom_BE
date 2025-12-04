package com.cntt.rentalmanagement.repository;

import com.cntt.rentalmanagement.domain.models.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MaintenanceRepository extends JpaRepository<Maintenance, Long>, MaintenanceRepositoryCustom {
}
