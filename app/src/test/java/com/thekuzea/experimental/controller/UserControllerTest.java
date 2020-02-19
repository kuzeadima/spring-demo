package com.thekuzea.experimental.controller;

import com.thekuzea.experimental.domain.dto.UserResource;
import com.thekuzea.experimental.exception.UserNotFoundException;
import com.thekuzea.experimental.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static com.thekuzea.experimental.constant.UserMessages.USER_ALREADY_EXISTS;
import static com.thekuzea.experimental.constant.UserMessages.USER_NOT_FOUND;
import static com.thekuzea.experimental.util.UserTestDataGenerator.createUserResource;
import static com.thekuzea.experimental.util.UserTestDataGenerator.createUserResourceList;
import static com.thekuzea.experimental.util.UserTestDataGenerator.createUserResourceWithErrorMessage;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    public void shouldReturnAllUsers() throws Exception {
        final List<UserResource> expectedUserResourceList = createUserResourceList();
        final String expectedUserResourceListJson = objectMapper.writeValueAsString(expectedUserResourceList);
        given(userService.getAllUsers()).willReturn(expectedUserResourceList);

        mvc.perform(get("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedUserResourceListJson));
    }

    @Test
    public void shouldReturnUserByUsername() throws Exception {
        final String username = "userN1";
        final UserResource expectedUserResource = createUserResource();
        final String expectedUserResourceJson = objectMapper.writeValueAsString(expectedUserResource);
        given(userService.getByUsername(username)).willReturn(expectedUserResource);

        mvc.perform(get("/user/{username}", username)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedUserResourceJson));
    }

    @Test
    public void shouldNotReturnUserByUsername() throws Exception {
        final String username = "unknown";
        given(userService.getByUsername(username)).willThrow(new UserNotFoundException());

        mvc.perform(get("/user/{username}", username)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(USER_NOT_FOUND));
    }

    @Test
    public void shouldSaveNewUser() throws Exception {
        final UserResource expectedUserResource = createUserResource();
        final String expectedUserResourceJson = objectMapper.writeValueAsString(expectedUserResource);
        given(userService.addNewUser(any(UserResource.class))).willReturn(expectedUserResource);

        mvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(expectedUserResourceJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedUserResourceJson));
    }

    @Test
    public void shouldNotSaveNewUser() throws Exception {
        final UserResource expectedUserResource = createUserResourceWithErrorMessage(USER_ALREADY_EXISTS);
        final String expectedUserResourceJson = objectMapper.writeValueAsString(expectedUserResource);
        given(userService.addNewUser(any(UserResource.class))).willReturn(expectedUserResource);

        mvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(expectedUserResourceJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedUserResourceJson));
    }

    @Test
    public void shouldUpdateUserByUserId() throws Exception {
        final String userId = UUID.randomUUID().toString();
        final UserResource expectedUserResource = createUserResource();
        final String expectedUserResourceJson = objectMapper.writeValueAsString(expectedUserResource);
        given(userService.updateByUserId(anyString(), any(UserResource.class))).willReturn(expectedUserResource);

        mvc.perform(put("/user/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(expectedUserResourceJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(content().json(expectedUserResourceJson));
    }

    @Test
    public void shouldNotUpdateUserByUserIdUserNotFound() throws Exception {
        final String userId = UUID.randomUUID().toString();
        final UserResource expectedUserResource = createUserResource();
        final String expectedUserResourceJson = objectMapper.writeValueAsString(expectedUserResource);
        given(userService.updateByUserId(anyString(), any(UserResource.class))).willThrow(new UserNotFoundException());

        mvc.perform(put("/user/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(expectedUserResourceJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(USER_NOT_FOUND));
    }

    @Test
    public void shouldDeleteUserByUsername() throws Exception {
        final String username = "userN1";
        final UserResource expectedUserResource = createUserResource();
        final String expectedUserResourceJson = objectMapper.writeValueAsString(expectedUserResource);
        given(userService.deleteByUsername(username)).willReturn(expectedUserResource);

        mvc.perform(delete("/user/{username}", username)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(content().json(expectedUserResourceJson));
    }

    @Test
    public void shouldNotDeleteUserByUsername() throws Exception {
        final String username = "unknown";
        given(userService.deleteByUsername(username)).willThrow(new UserNotFoundException());

        mvc.perform(delete("/user/{username}", username)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(USER_NOT_FOUND));
    }
}
