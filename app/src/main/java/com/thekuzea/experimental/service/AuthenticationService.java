package com.thekuzea.experimental.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationService {

    UserDetails getCurrentLoggedInUser();
}
