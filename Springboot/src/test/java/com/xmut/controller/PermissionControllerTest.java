package com.xmut.controller;

import com.xmut.entity.Permission;
import com.xmut.service.PermissionService;
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
public class PermissionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PermissionService permissionService;

    @InjectMocks
    private PermissionController permissionController;

    public PermissionControllerTest() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(permissionController).build();
    }

    @Test
    public void testGetAllPermissions() throws Exception {
        when(permissionService.getAllPermissions()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/permissions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testAddPermission() throws Exception {
        Permission permission = new Permission();
        when(permissionService.addPermission(permission)).thenReturn(true);
        mockMvc.perform(post("/permissions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Add successful"));
    }

    @Test
    public void testUpdatePermission() throws Exception {
        Permission permission = new Permission();
        when(permissionService.updatePermission(permission)).thenReturn(true);
        mockMvc.perform(put("/permissions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Update successful"));
    }

    @Test
    public void testDeletePermission() throws Exception {
        when(permissionService.deletePermission(1)).thenReturn(true);
        mockMvc.perform(delete("/permissions/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Delete successful"));
    }
}
