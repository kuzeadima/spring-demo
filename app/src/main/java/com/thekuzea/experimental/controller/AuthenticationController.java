package com.thekuzea.experimental.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thekuzea.experimental.domain.dto.TokenDto;
import com.thekuzea.experimental.domain.dto.UserDto;
import com.thekuzea.experimental.domain.dto.validation.NotBlankRequired;
import com.thekuzea.experimental.domain.dto.validation.SizeRequired;

@RequestMapping("/auth")
public interface AuthenticationController {

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<TokenDto> authenticate(@Validated({NotBlankRequired.class, SizeRequired.class})
                                          @RequestBody UserDto resource);
}
