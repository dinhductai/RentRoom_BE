package com.cntt.rentalmanagement.repository;

import com.cntt.rentalmanagement.domain.models.BlogStore;
import com.cntt.rentalmanagement.domain.models.Room;
import com.cntt.rentalmanagement.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface BlogStoreRepository extends JpaRepository<BlogStore, Long>,BlogStoreRepositoryCustom {
    Optional<BlogStore> findByRoomAndUser(Room room, User user);
}
