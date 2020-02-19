package com.thekuzea.experimental.controller;

import com.thekuzea.experimental.domain.dto.RoleResource;
import com.thekuzea.experimental.exception.RoleNotFoundException;
import com.thekuzea.experimental.service.RoleService;
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

import static com.thekuzea.experimental.constant.RoleMessages.ROLE_NOT_FOUND;
import static com.thekuzea.experimental.util.RoleTestDataGenerator.createRoleResource;
import static com.thekuzea.experimental.util.RoleTestDataGenerator.createRoleResourceList;
import static com.thekuzea.experimental.util.RoleTestDataGenerator.createRoleResourceWithErrorMessage;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = RoleController.class)
public class RoleControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoleService roleService;

    @Test
    public void shouldReturnAllRoles() throws Exception {
        final List<RoleResource> expectedRoleResourceList = createRoleResourceList();
        final String expectedRoleResourceListJson = objectMapper.writeValueAsString(expectedRoleResourceList);
        given(roleService.getAllRoles()).willReturn(expectedRoleResourceList);

        mvc.perform(get("/role")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedRoleResourceListJson));
    }

    @Test
    public void shouldSaveNewRole() throws Exception {
        final RoleResource expectedRoleResource = createRoleResource();
        final String expectedRoleResourceJson = objectMapper.writeValueAsString(expectedRoleResource);
        given(roleService.addNewRole(any(RoleResource.class))).willReturn(expectedRoleResource);

        mvc.perform(post("/role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(expectedRoleResourceJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedRoleResourceJson));
    }

    @Test
    public void shouldNotSaveNewRole() throws Exception {
        final RoleResource expectedRoleResource = createRoleResourceWithErrorMessage();
        final String expectedRoleResourceJson = objectMapper.writeValueAsString(expectedRoleResource);
        given(roleService.addNewRole(any(RoleResource.class))).willReturn(expectedRoleResource);

        mvc.perform(post("/role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(expectedRoleResourceJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedRoleResourceJson));
    }

    @Test
    public void shouldDeleteRoleByName() throws Exception {
        final String name = "user";
        final RoleResource expectedRoleResource = createRoleResource();
        final String expectedRoleResourceJson = objectMapper.writeValueAsString(expectedRoleResource);
        given(roleService.deleteByName(name)).willReturn(expectedRoleResource);

        mvc.perform(delete("/role/{name}", name)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(content().json(expectedRoleResourceJson));
    }

    @Test
    public void shouldNotDeleteRoleByName() throws Exception {
        final String name = "unknown";
        given(roleService.deleteByName(name)).willThrow(new RoleNotFoundException());

        mvc.perform(delete("/role/{name}", name)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ROLE_NOT_FOUND));
    }
}
