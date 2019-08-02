package com.example.polls.service;

import com.example.polls.exception.AppException;
import com.example.polls.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    public UserPrincipal getUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            throw new AppException("Authentication not found");

        return (UserPrincipal)authentication.getPrincipal();
    }
}
