package com.xmut.controller;

import com.xmut.service.SystemService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class SystemControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SystemService systemService;

    @InjectMocks
    private SystemController systemController;

    public SystemControllerTest() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(systemController).build();
    }

    @Test
    public void testGetDashboardOverview() throws Exception {
        when(systemService.getDashboardOverview()).thenReturn(Map.of());
        mockMvc.perform(get("/system/dashboard/overview"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetLatestMessages() throws Exception {
        when(systemService.getLatestMessages()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/system/messages/latest"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetSystemInfo() throws Exception {
        when(systemService.getSystemInfo()).thenReturn(Map.of());
        mockMvc.perform(get("/system/info"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
