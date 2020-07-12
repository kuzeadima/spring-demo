package com.thekuzea.experimental.controller.impl;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.thekuzea.experimental.controller.UserController;
import com.thekuzea.experimental.domain.dto.UserDto;
import com.thekuzea.experimental.service.UserService;

@RestController
@RequiredArgsConstructor
public class DefaultUserController implements UserController {

    private final UserService userService;

    @Override
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Override
    public ResponseEntity<UserDto> getByUsername(final String username) {
        return ResponseEntity.ok(userService.getByUsername(username));
    }

    @Override
    public ResponseEntity<UserDto> addNewUser(final UserDto incomingResource) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addNewUser(incomingResource));
    }

    @Override
    public ResponseEntity<UserDto> updateByUserId(final String userId, final UserDto incomingResource) {
        return ResponseEntity.accepted().body(userService.updateByUserId(userId, incomingResource));
    }

    @Override
    public ResponseEntity<Void> deleteByUsername(final String username) {
        userService.deleteByUsername(username);
        return ResponseEntity.accepted().build();
    }
}
