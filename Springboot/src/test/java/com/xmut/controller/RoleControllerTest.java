package com.xmut.controller;

import com.xmut.entity.Role;
import com.xmut.service.RoleService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class RoleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    public RoleControllerTest() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(roleController).build();
    }

    @Test
    public void testGetAllRoles() throws Exception {
        when(roleService.getAllRoles()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/roles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testAddRole() throws Exception {
        Role role = new Role();
        when(roleService.addRole(role)).thenReturn(true);
        mockMvc.perform(post("/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Add successful"));
    }

    @Test
    public void testUpdateRole() throws Exception {
        Role role = new Role();
        when(roleService.updateRole(role)).thenReturn(true);
        mockMvc.perform(put("/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Update successful"));
    }

    @Test
    public void testDeleteRole() throws Exception {
        when(roleService.deleteRole(1)).thenReturn(true);
        mockMvc.perform(delete("/roles/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Delete successful"));
    }
}
