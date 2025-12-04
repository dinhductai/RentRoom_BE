package com.cntt.rentalmanagement.services;

import com.cntt.rentalmanagement.repository.UserRepository;
import com.cntt.rentalmanagement.secruity.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class BaseService {
    @Autowired
    protected UserRepository userRepository;

    public String getUsername() {
        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return user.getUsername();
    }

    public Long getUserId() {
        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return user.getId();
    }

}
