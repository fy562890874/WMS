package com.xmut.controller;

import com.xmut.entity.Warehouse;
import com.xmut.service.WarehouseService;
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
public class WarehouseControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WarehouseService warehouseService;

    @InjectMocks
    private WarehouseController warehouseController;

    public WarehouseControllerTest() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(warehouseController).build();
    }

    @Test
    public void testGetAllWarehouses() throws Exception {
        when(warehouseService.getAllWarehouses()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/warehouses"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testAddWarehouse() throws Exception {
        Warehouse warehouse = new Warehouse();
        when(warehouseService.addWarehouse(warehouse)).thenReturn(true);
        mockMvc.perform(post("/warehouses")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Add successful"));
    }

    @Test
    public void testUpdateWarehouse() throws Exception {
        Warehouse warehouse = new Warehouse();
        when(warehouseService.updateWarehouse(warehouse)).thenReturn(true);
        mockMvc.perform(put("/warehouses")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Update successful"));
    }

    @Test
    public void testDeleteWarehouse() throws Exception {
        when(warehouseService.deleteWarehouse(1)).thenReturn(true);
        mockMvc.perform(delete("/warehouses/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Delete successful"));
    }
}
