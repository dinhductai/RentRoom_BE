package com.cntt.rentalmanagement.services;




import com.cntt.rentalmanagement.domain.payload.request.*;
import com.cntt.rentalmanagement.domain.payload.response.AuthResponse;
import com.cntt.rentalmanagement.domain.payload.response.MessageResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URI;

public interface AuthService {
    URI registerAccount(SignUpRequest signUpRequest) throws MessagingException, IOException;

    String login(LoginRequest loginRequest);
    
    AuthResponse buildAuthResponse(String token);

    MessageResponse forgotPassword(EmailRequest emailRequest) throws MessagingException, IOException;

    MessageResponse resetPassword(ResetPasswordRequest resetPasswordRequest);

    MessageResponse confirmedAccount(EmailRequest emailRequest);

    MessageResponse changePassword(ChangePasswordRequest changePasswordRequest);

    MessageResponse changeImage(MultipartFile file);

    MessageResponse lockAccount(Long id);

    MessageResponse uploadProfile(MultipartFile file, String zalo, String facebook, String address);
}
