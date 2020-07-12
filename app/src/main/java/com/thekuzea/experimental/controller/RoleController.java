package com.thekuzea.experimental.controller;

import java.util.List;
import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thekuzea.experimental.domain.dto.RoleDto;

@PreAuthorize("hasAuthority('admin')")
@RequestMapping("/role")
public interface RoleController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<RoleDto>> getAllRoles();

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<RoleDto> addNewRole(@Valid @RequestBody RoleDto incomingResource);

    @DeleteMapping(value = "/{name}")
    ResponseEntity<Void> deleteByName(@PathVariable("name") String name);
}
