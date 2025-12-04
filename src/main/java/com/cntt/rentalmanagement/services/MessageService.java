package com.cntt.rentalmanagement.services;

public interface MessageService {
	
	void Producer(String senderName, String receiverName);
	
	void Consumer(String senderName, String receiverName);
	

}
