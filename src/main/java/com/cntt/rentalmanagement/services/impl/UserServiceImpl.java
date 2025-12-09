package com.cntt.rentalmanagement.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cntt.rentalmanagement.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cntt.rentalmanagement.domain.models.Message;
import com.cntt.rentalmanagement.domain.models.MessageChat;
import com.cntt.rentalmanagement.domain.models.User;
import com.cntt.rentalmanagement.domain.models.DTO.MessageDTO;
import com.cntt.rentalmanagement.exception.ResourceNotFoundException;
import com.cntt.rentalmanagement.repository.MessageChatRepository;
import com.cntt.rentalmanagement.repository.MessageRepository;
import com.cntt.rentalmanagement.repository.UserRepository;
import com.cntt.rentalmanagement.services.UserService;

@Service
public class UserServiceImpl extends BaseService implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	FileStorageServiceImpl fileStorageServiceImpl;

	@Autowired
	MessageRepository messageRepository;

	@Autowired
	MessageChatRepository messageChatRepository;

	@Override
	public MessageDTO toMessageDTO(User user, Message message) {
		String userName = "";
		String imageUrl = "";
		String last_message_text = "";
		if (message.getReceiver().getEmail().equals(user.getEmail())) {
			userName = message.getSender().getName();
			imageUrl = message.getSender().getImageUrl();
		} else {
			userName = message.getReceiver().getName();
			imageUrl = message.getReceiver().getImageUrl();
		}
		last_message_text = message.getContent().get(message.getContent().size() - 1).getContent();
		return new MessageDTO(1L, userName, imageUrl, last_message_text);
	}

	@Override
	public User getUserById(Long id) {
		return userRepository.findById(id).get();
	}

	@Override
	public String updateImageUser(Long id, String image) {
		try {
			User user = userRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
			user.setImageUrl(image);
			userRepository.save(user);

			return "Cập nhật hình ảnh thành công!!!";
		} catch (Exception e) {
			return "Cập nhật hình ảnh thất bại!!!";
		}
	}

	@Override
	public String updateUser(User user) {
		try {
			User userCore = userRepository.findById(user.getId())
					.orElseThrow(() -> new ResourceNotFoundException("User", "id", user.getId()));

			if (user.getAddress() != null)
				userCore.setAddress(user.getAddress());
			if (user.getName() != null)
				userCore.setName(user.getName());

			// Kiểm tra phone unique constraint trước khi cập nhật
			if (user.getPhone() != null && !user.getPhone().isEmpty()) {
				// Nếu phone thay đổi, kiểm tra xem phone mới có trùng với user khác không
				if (!user.getPhone().equals(userCore.getPhone())) {
					userRepository.findByPhone(user.getPhone()).ifPresent(existingUser -> {
						if (!existingUser.getId().equals(userCore.getId())) {
							throw new RuntimeException("Số điện thoại đã được sử dụng bởi người dùng khác");
						}
					});
				}
				userCore.setPhone(user.getPhone());
			}

			userRepository.save(userCore);
			return "Cập nhật thông tin thành công!!!";
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
			return "Người dùng không tồn tại!!!";
		} catch (RuntimeException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
			return "Cập nhật thông tin thất bại: " + e.getMessage();
		}
	}

	@Override
	public List<MessageDTO> getMessageUser() {
		try {
			User user = userRepository.findById(getUserId()).get();
			List<MessageDTO> result = new ArrayList<>();
			for (Message message : messageRepository.findBySender(user)) {
				if (message.getReceiver().getId() != message.getSender().getId()) {
					String lastMessage = message.getContent().get(message.getContent().size() - 1).getContent();
					if (message.getContent().get(message.getContent().size() - 1).getSendBy()) {
						if (getUserId() == message.getReceiver().getId())
							lastMessage = "Bạn: " + lastMessage;
					} else {
						if (getUserId() == message.getSender().getId())
							lastMessage = "Bạn: " + lastMessage;
					}
					result.add(new MessageDTO(message.getReceiver().getId(), message.getReceiver().getName(),
							message.getReceiver().getImageUrl(), lastMessage));
				}
			}
			for (Message message : messageRepository.findByReceiver(user)) {
				if (message.getReceiver().getId() != message.getSender().getId()) {
					String lastMessage = message.getContent().get(message.getContent().size() - 1).getContent();
					if (message.getContent().get(message.getContent().size() - 1).getSendBy()) {
						if (getUserId() == message.getReceiver().getId())
							lastMessage = "Bạn: " + lastMessage;
					} else {
						if (getUserId() == message.getSender().getId())
							lastMessage = "Bạn: " + lastMessage;
					}
					result.add(new MessageDTO(message.getSender().getId(), message.getSender().getName(),
							message.getSender().getImageUrl(), lastMessage));
				}
			}

			return result;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<User> findMessageUser(String userName) {
		List<User> result = new ArrayList<>();
		for (User user : userRepository.findAll()) {
			if (user.getName().toUpperCase().equals(userName.toUpperCase()))
				result.add(user);
		}
		return result;
	}

	@Override
	public Message getMessageChatUser(Long userId, Long guestId) {
		try {
			User user = userRepository.findById(Math.min(userId, guestId)).get();
			User guest = userRepository.findById(Math.max(userId, guestId)).get();
			Message message = messageRepository.findBySenderAndReceiver(user, guest);
			if (message != null) {
				return message;
			} else {
				message = new Message();
				message.setSender(user);
				message.setReceiver(guest);
				messageRepository.save(message);
			}
			return message;
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public String addChatUser(Long id, Long userId, MessageChat messageChat) {
		try {
			User sender = userRepository.findById(Math.min(id, userId)).get();
			User receiver = userRepository.findById(Math.max(id, userId)).get();
			Message message = messageRepository.findBySenderAndReceiver(sender, receiver);
			MessageChat messageChat2 = new MessageChat();
			messageChat2.setContent(messageChat.getContent());
			messageChat2.setMessage(message);
			messageChat2.setRead(false);
			messageChat2.setSendBy(id > userId ? true : false);
			messageChat2.setSentAt(new Date());
			messageChatRepository.save(messageChat2);
			return "Gửi tin nhắn thành công!!!";
		} catch (Exception e) {
			System.out.println(e.toString());
			return "Gửi tin nhắn thất bại!!!";
		}
	}

	@Override
	public Page<User> getRentalers(String name, Integer pageNo, Integer pageSize) {
		try {
			Pageable pageable = PageRequest.of(pageNo, pageSize);
			return userRepository.findRentalers(name, pageable);
		} catch (Exception e) {
			throw new ResourceNotFoundException("Error fetching rentalers", "name", name);
		}
	}

	@Override
	public List<User> searchUsersByName(String userName) {
		try {
			return userRepository.findByNameContainingIgnoreCase(userName);
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

}
