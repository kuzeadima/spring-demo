package com.thekuzea.experimental.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thekuzea.experimental.domain.dto.UserDto;
import com.thekuzea.experimental.domain.dto.validation.NotBlankRequired;
import com.thekuzea.experimental.domain.dto.validation.SizeRequired;

@RequestMapping("/user")
public interface UserController {

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<UserDto>> getAllUsers();

    @GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserDto> getByUsername(@PathVariable("username") String username);

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserDto> addNewUser(@Validated({NotBlankRequired.class, SizeRequired.class})
                                       @RequestBody UserDto incomingResource);

    @PutMapping(value = "/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserDto> updateByUserId(@PathVariable("userId") String userId,
                                           @Validated(SizeRequired.class)
                                           @RequestBody UserDto incomingResource);

    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping(value = "/{username}")
    ResponseEntity<Void> deleteByUsername(@PathVariable("username") String username);
}
