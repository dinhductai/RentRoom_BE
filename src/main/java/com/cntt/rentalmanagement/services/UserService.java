package com.cntt.rentalmanagement.services;

import java.util.List;

import com.cntt.rentalmanagement.domain.models.Message;
import com.cntt.rentalmanagement.domain.models.MessageChat;
import com.cntt.rentalmanagement.domain.models.User;
import com.cntt.rentalmanagement.domain.models.DTO.MessageDTO;

public interface UserService {
	User getUserById(Long id);

	String updateImageUser(Long id, String image);

	String updateUser(User user);

	List<MessageDTO> getMessageUser();

	MessageDTO toMessageDTO(User user, Message message);

	List<User> findMessageUser(String userName);

	Message getMessageChatUser(Long userId, Long guestId);

	String addChatUser(Long id, Long userId, MessageChat messageChat);
}
