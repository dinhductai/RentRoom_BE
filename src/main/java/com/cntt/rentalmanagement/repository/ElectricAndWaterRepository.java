package com.cntt.rentalmanagement.repository;

import com.cntt.rentalmanagement.domain.models.ElectricAndWater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElectricAndWaterRepository extends JpaRepository<ElectricAndWater, Long>{
    List<ElectricAndWater> findByRoomId(Long roomId);
}
