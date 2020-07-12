package com.thekuzea.experimental.util;

import java.util.Collections;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static com.thekuzea.experimental.config.mock.SecurityMockConfig.INCORRECT_PASSWORD;
import static com.thekuzea.experimental.config.mock.SecurityMockConfig.PASSWORD;
import static com.thekuzea.experimental.config.mock.SecurityMockConfig.ROLE;
import static com.thekuzea.experimental.config.mock.SecurityMockConfig.ROOT_USERNAME;
import static com.thekuzea.experimental.config.mock.SecurityMockConfig.SIMPLE_USER_USERNAME;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthenticationTestDataGenerator {

    public static Authentication createUnsuccessfulAuthentication() {
        return new UsernamePasswordAuthenticationToken(ROOT_USERNAME, INCORRECT_PASSWORD);
    }

    public static UserDetails createUserDetailsForRootUser() {
        return new User(ROOT_USERNAME, PASSWORD, createAuthoritiesForRootUser());
    }

    public static UserDetails createUserDetailsForSimpleUser() {
        return new User(SIMPLE_USER_USERNAME, PASSWORD, Collections.emptyList());
    }

    private static List<GrantedAuthority> createAuthoritiesForRootUser() {
        return Collections.singletonList(new SimpleGrantedAuthority(ROLE));
    }
}
