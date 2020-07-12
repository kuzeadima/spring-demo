package com.thekuzea.experimental.config.mock;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.thekuzea.experimental.util.AuthenticationTestDataGenerator;
import com.thekuzea.experimental.util.JwtTokenUtil;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestConfiguration
public class SecurityMockConfig {

    private static final String TOKEN = "dG9rZW4=";

    public static final String ROOT_USERNAME = "root";

    public static final String SIMPLE_USER_USERNAME = "Larry";

    public static final String ROLE = "admin";

    public static final String PASSWORD = "password123";

    public static final String INCORRECT_PASSWORD = "password";

    @Bean
    @Primary
    public AuthenticationManager authenticationManager() {
        final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);

        final Authentication unsuccessfulAuthentication = AuthenticationTestDataGenerator.createUnsuccessfulAuthentication();
        when(authenticationManager.authenticate(refEq(unsuccessfulAuthentication)))
                .thenThrow(new BadCredentialsException(StringUtils.EMPTY));

        return authenticationManager;
    }

    @Bean
    @Primary
    public UserDetailsService jwtUserDetailsService() {
        final UserDetailsService jwtUserDetailsService = mock(UserDetailsService.class);

        when(jwtUserDetailsService.loadUserByUsername(ROOT_USERNAME)).thenReturn(AuthenticationTestDataGenerator.createUserDetailsForRootUser());

        return jwtUserDetailsService;
    }

    @Bean
    @Primary
    public JwtTokenUtil jwtTokenUtil() {
        final JwtTokenUtil jwtTokenUtil = mock(JwtTokenUtil.class);

        when(jwtTokenUtil.generateToken(any(UserDetails.class))).thenReturn(TOKEN);
        when(jwtTokenUtil.getUsernameFromToken(TOKEN)).thenReturn(ROOT_USERNAME);
        when(jwtTokenUtil.validateToken(eq(TOKEN), any(UserDetails.class))).thenReturn(true);

        return jwtTokenUtil;
    }
}
