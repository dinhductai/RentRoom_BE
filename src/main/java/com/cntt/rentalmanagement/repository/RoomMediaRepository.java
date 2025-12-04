package com.cntt.rentalmanagement.repository;

import com.cntt.rentalmanagement.domain.models.Room;
import com.cntt.rentalmanagement.domain.models.RoomMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomMediaRepository extends JpaRepository<RoomMedia, Long> {

    void deleteAllByRoom(Room room);
}
