package com.cntt.rentalmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cntt.rentalmanagement.domain.models.Category;
import com.cntt.rentalmanagement.domain.models.MessageChat;

@Repository
public interface MessageChatRepository extends JpaRepository<MessageChat, Long> {
}
