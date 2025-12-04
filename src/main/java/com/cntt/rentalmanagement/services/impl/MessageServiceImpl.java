package com.cntt.rentalmanagement.services.impl;

import java.util.Date;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.stereotype.Service;

import com.cntt.rentalmanagement.services.MessageService;

@Service
public class MessageServiceImpl implements MessageService{

	@Override
	public void Producer(String senderName, String receiverName) {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection connection;
        Session session;
        try {
            connection = connectionFactory.createConnection();
            connection.start();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Tạo và sử dụng hàng đợi động dựa vào tên người gửi và người nhận
            Queue senderQueue = session.createQueue(senderName);
            Queue receiverQueue = session.createQueue(receiverName);
            
            // Tạo producer để gửi tin nhắn tới hàng đợi của người nhận
            MessageProducer producer = session.createProducer(receiverQueue);

            // Tạo và gửi tin nhắn
            TextMessage message = session.createTextMessage("Hello, this is a chat message!");
            message.setStringProperty("senderName", senderName);
            producer.send(message);

            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
		
	}

	@Override
	public void Consumer(String senderName, String receiverName) {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection connection;
        Session session;
        try {
            connection = connectionFactory.createConnection();
            connection.start();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue receiverQueue = session.createQueue(receiverName);
            MessageConsumer consumer = session.createConsumer(receiverQueue);

            consumer.setMessageListener(message -> {
                if (message instanceof TextMessage) {
                    try {
                        String sender = message.getStringProperty("senderName");
                        String content = ((TextMessage) message).getText();
                        Date sentAt = new Date(message.getJMSTimestamp());
                        
                        System.out.println("Received message from " + sender + ": " + content + " (Sent at: " + sentAt + ")");
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });

            Thread.sleep(10);

            connection.close();
        } catch (JMSException | InterruptedException e) {
            e.printStackTrace();
        }
		
	}

}
