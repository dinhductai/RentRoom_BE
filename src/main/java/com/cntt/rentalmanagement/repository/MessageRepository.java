package com.cntt.rentalmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cntt.rentalmanagement.domain.models.Message;
import com.cntt.rentalmanagement.domain.models.User;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>{

    Message findBySenderAndReceiver(User sender, User receiver);

    List<Message> findBySender(User sender);

    List<Message> findByReceiver(User receiver);

}
