package com.thekuzea.experimental.controller.impl;

import com.thekuzea.experimental.controller.UserController;
import com.thekuzea.experimental.domain.dto.UserResource;
import com.thekuzea.experimental.exception.UserNotFoundException;
import com.thekuzea.experimental.service.UserService;
import com.thekuzea.experimental.constant.UserMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getByUsername(final String username) {
        try {
            final UserResource userResource = userService.getByUsername(username);
            return new ResponseEntity<>(userResource, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(UserMessages.USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> addNewUser(final UserResource incomingResource) {
        final UserResource outgoingResource = userService.addNewUser(incomingResource);

        if (outgoingResource.getValidationMessages().isEmpty()) {
            return new ResponseEntity<>(outgoingResource, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(outgoingResource, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> updateByUserId(final String userId, final UserResource incomingResource) {
        try {
            final UserResource userResource = userService.updateByUserId(userId, incomingResource);

            if(userResource.getValidationMessages().isEmpty()) {
                return new ResponseEntity<>(userResource, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>(userResource, HttpStatus.BAD_REQUEST);
            }
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(UserMessages.USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> deleteByUsername(final String username) {
        try {
            final UserResource userResource = userService.deleteByUsername(username);
            return new ResponseEntity<>(userResource, HttpStatus.ACCEPTED);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(UserMessages.USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
    }
}
