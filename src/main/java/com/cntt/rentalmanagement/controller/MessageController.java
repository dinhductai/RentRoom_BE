package com.cntt.rentalmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.cntt.rentalmanagement.domain.models.Message;
import com.cntt.rentalmanagement.domain.models.User;
import com.cntt.rentalmanagement.domain.models.DTO.MessageDTO;
import com.cntt.rentalmanagement.repository.MessageChatRepository;
import com.cntt.rentalmanagement.repository.MessageRepository;
import com.cntt.rentalmanagement.repository.UserRepository;
import com.cntt.rentalmanagement.secruity.CurrentUser;
import com.cntt.rentalmanagement.secruity.UserPrincipal;
import com.cntt.rentalmanagement.services.impl.MessageServiceImpl;
import com.cntt.rentalmanagement.services.impl.UserServiceImpl;

@RestController
public class MessageController {

	@Autowired
	private MessageServiceImpl messageServiceImpl;

	@Autowired
	private UserServiceImpl userServiceImpl;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private MessageChatRepository messageChatRepository;

	@GetMapping("/send")
	public Message testSendMessage() {
		// String email1 = "giangnam.trunghoccoso@gmail.com", email2 =
		// "giangnam.trunghocphothong@gmail.com";
		// messageServiceImpl.Producer(email1, email2);
		// messageServiceImpl.Consumer(email1, email2);
		// User sender = userRepository.findByEmail(email1).get();
		// User receiver = userRepository.findByEmail(email2).get();
		// Message message = messageRepository.findBySenderAndReceiver(sender,
		// receiver);
		// System.out.println(message.getId());
		// List <MessageChat> messageChats = message.getContent();
		// if (!(messageChats != null)) messageChats = new ArrayList<>();
		// MessageChat messageChat = new MessageChat();
		// messageChat.setContent("test tinh nang!!!");
		// messageChat.setMessage(message);
		// messageChat.setRead(false);
		// messageChat.setSendBy(true);
		// messageChat.setSentAt(new Date());
		// messageChats.add(messageChat);
		// messageChatRepository.save(messageChat);
		// message.setContent(messageChats);
		//
		// messageRepository.save(message);
		// return "success";
		return userServiceImpl.getMessageChatUser(Long.valueOf(1), Long.valueOf(2));
	}

	@GetMapping("/user/message")
	@PreAuthorize("hasRole('USER') or hasRole('RENTALER')")
	public List<MessageDTO> getMessageUser() {
		return userServiceImpl.getMessageUser();
	}

	@GetMapping("/user/message/{userName}")
	@PreAuthorize("hasRole('USER') or hasRole('RENTALER')")
	public List<User> findMessageUser(@CurrentUser UserPrincipal userPrincipal, @PathVariable String userName) {
		return userServiceImpl.findMessageUser(userName);
	}

	@GetMapping("/user/message-chat/{userId}")
	@PreAuthorize("hasRole('USER') or hasRole('RENTALER')")
	public Message getMessageChatUser(@CurrentUser UserPrincipal userPrincipal, @PathVariable Long userId) {
		return userServiceImpl.getMessageChatUser(userPrincipal.getId(), userId);
	}
}
