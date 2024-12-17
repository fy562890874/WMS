package com.xmut.controller;

import com.xmut.entity.Product;
import com.xmut.service.ProductService;
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
public class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    public ProductControllerTest() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    public void testGetAllProducts() throws Exception {
        when(productService.getAllProducts()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testAddProduct() throws Exception {
        Product product = new Product();
        when(productService.addProduct(product)).thenReturn(true);
        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Add successful"));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Product product = new Product();
        when(productService.updateProduct(product)).thenReturn(true);
        mockMvc.perform(put("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Update successful"));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        when(productService.deleteProduct(1)).thenReturn(true);
        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Delete successful"));
    }

    @Test
    public void testAdjustStock() throws Exception {
        when(productService.adjustStock(1, 1, 10)).thenReturn(true);
        mockMvc.perform(post("/products/adjustStock")
                .param("productId", "1")
                .param("warehouseId", "1")
                .param("quantity", "10"))
                .andExpect(status().isOk())
                .andExpect(content().string("Stock adjustment successful"));
    }

    @Test
    public void testUploadImage() throws Exception {
        when(productService.uploadImage(1, "imageUrl")).thenReturn(true);
        mockMvc.perform(post("/products/uploadImage")
                .param("productId", "1")
                .param("imageUrl", "imageUrl"))
                .andExpect(status().isOk())
                .andExpect(content().string("Image upload successful"));
    }
}
