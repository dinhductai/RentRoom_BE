package com.cntt.rentalmanagement.secruity;


import com.cntt.rentalmanagement.domain.models.User;
import com.cntt.rentalmanagement.exception.BadRequestException;
import com.cntt.rentalmanagement.exception.ResourceNotFoundException;
import com.cntt.rentalmanagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Email của bạn không tồn tại : " + email)
                );
        if (Boolean.TRUE.equals(user.getIsLocked()))
        {
            throw new BadRequestException("Tài khoản của bạn đã bị khóa. Lý do chi tiết sẽ có trong email của bạn.");
        }
        if (Boolean.FALSE.equals(user.getIsConfirmed())) {
            throw new BadRequestException("Tài khoản của bạn chưa đuợc xác thực!!!");
        }

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );

        logger.info("📋 Loading user by ID: {}", id);
        logger.info("📧 User email: {}", user.getEmail());
        logger.info("🎭 User roles count: {}", user.getRoles().size());
        logger.info("🎭 User roles: {}", user.getRoles());

        UserPrincipal userPrincipal = UserPrincipal.create(user);
        
        logger.info("✅ UserPrincipal created with authorities: {}", userPrincipal.getAuthorities());

        return userPrincipal;
    }
}