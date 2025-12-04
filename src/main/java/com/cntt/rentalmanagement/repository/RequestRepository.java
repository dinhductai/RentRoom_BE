package com.cntt.rentalmanagement.repository;

import com.cntt.rentalmanagement.domain.models.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface RequestRepository extends JpaRepository<Request, Long>, RequestCustomRepository {
}
