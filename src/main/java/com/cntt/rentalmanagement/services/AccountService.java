package com.cntt.rentalmanagement.services;

import com.cntt.rentalmanagement.domain.models.User;
import com.cntt.rentalmanagement.domain.payload.request.RoleRequest;
import com.cntt.rentalmanagement.domain.payload.request.SendEmailRequest;
import com.cntt.rentalmanagement.domain.payload.response.MessageResponse;
import com.cntt.rentalmanagement.domain.payload.response.UserResponse;
import org.springframework.data.domain.Page;

import javax.mail.MessagingException;
import java.io.IOException;

public interface AccountService {

    Page<User> getAllAccount(String keyword, Integer pageNo, Integer pageSize);

    User getAccountById(Long id);

    MessageResponse sendEmailForRentaler(Long id, SendEmailRequest sendEmailRequest) throws MessagingException, IOException;

    MessageResponse divideAuthorization(Long id, RoleRequest roleRequest);

    MessageResponse sendEmailForRentaler(SendEmailRequest sendEmailRequest) throws MessagingException, IOException;

    MessageResponse sendEmailOfCustomer(SendEmailRequest sendEmailRequest) throws MessagingException, IOException;
}
