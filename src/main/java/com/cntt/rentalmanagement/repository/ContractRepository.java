package com.cntt.rentalmanagement.repository;

import com.cntt.rentalmanagement.domain.models.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ContractRepository extends JpaRepository<Contract, Long>, ContractRepositoryCustom {
    @Query(value = "SELECT sum(c.numOfPeople) from Contract c ")
    Long sumNumOfPeople();
    
    // Tìm contract theo renter_user_id (người thuê)
    // Sử dụng underscore để truy cập nested property: renterUser.id
    Page<Contract> findByRenterUser_Id(Long renterUserId, Pageable pageable);
}
