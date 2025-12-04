package com.cntt.rentalmanagement.repository;

import com.cntt.rentalmanagement.domain.models.Location;
import com.cntt.rentalmanagement.domain.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}
