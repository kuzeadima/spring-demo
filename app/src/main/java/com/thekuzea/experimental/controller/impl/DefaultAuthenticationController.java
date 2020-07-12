package com.thekuzea.experimental.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RestController;

import com.thekuzea.experimental.controller.AuthenticationController;
import com.thekuzea.experimental.domain.dto.TokenDto;
import com.thekuzea.experimental.domain.dto.UserDto;
import com.thekuzea.experimental.util.JwtTokenUtil;

@RestController
@RequiredArgsConstructor
public class DefaultAuthenticationController implements AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService jwtUserDetailsService;

    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public ResponseEntity<TokenDto> authenticate(final UserDto resource) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(resource.getUsername(), resource.getPassword())
        );

        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(resource.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new TokenDto(token));
    }
}
