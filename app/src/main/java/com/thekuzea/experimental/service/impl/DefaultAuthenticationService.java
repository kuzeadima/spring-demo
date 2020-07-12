package com.thekuzea.experimental.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.thekuzea.experimental.service.AuthenticationService;

@Service
public class DefaultAuthenticationService implements AuthenticationService {

    @Override
    public UserDetails getCurrentLoggedInUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetails) authentication.getPrincipal();
    }
}
