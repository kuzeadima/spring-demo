package com.thekuzea.experimental.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.thekuzea.experimental.constant.messages.entity.UserMessages;
import com.thekuzea.experimental.constant.messages.logging.LoggingMessages;
import com.thekuzea.experimental.domain.dao.UserRepository;
import com.thekuzea.experimental.domain.model.User;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final Optional<User> possibleUser = userRepository.findByUsername(username);

        if (possibleUser.isPresent()) {
            final User foundUser = possibleUser.get();
            final List<SimpleGrantedAuthority> authorities =
                    Collections.singletonList(new SimpleGrantedAuthority(foundUser.getRole().getName()));

            return new org.springframework.security.core.userdetails.User(
                    foundUser.getUsername(),
                    foundUser.getPassword(),
                    authorities
            );
        } else {
            log.debug(LoggingMessages.USER_NOT_FOUND_BY_USERNAME, username);
            throw new IllegalArgumentException(UserMessages.USER_NOT_FOUND);
        }
    }
}
