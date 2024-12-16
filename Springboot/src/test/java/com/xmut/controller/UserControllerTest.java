package com.xmut.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmut.entity.User;
import com.xmut.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() throws Exception {
        User user1 = new User();
        user1.setUserId(1);
        user1.setUsername("user1");
        user1.setRealName("用户一");
        user1.setStatus(true);

        User user2 = new User();
        user2.setUserId(2);
        user2.setUsername("user2");
        user2.setRealName("用户二");
        user2.setStatus(false);

        List<User> users = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].username").value("user2"));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void addUser_ShouldReturnSuccessMessage() throws Exception {
        User user = new User();
        user.setUsername("newuser");
        user.setRealName("新用户");
        user.setPassword("password");

        when(userService.addUser(any(User.class))).thenReturn(true);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("Add successful"));

        verify(userService, times(1)).addUser(any(User.class));
    }

    @Test
    void updateUser_ShouldReturnSuccessMessage() throws Exception {
        User user = new User();
        user.setUserId(1);
        user.setUsername("updateduser");
        user.setRealName("更新用户");

        when(userService.updateUser(any(User.class))).thenReturn(true);

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("Update successful"));

        verify(userService, times(1)).updateUser(any(User.class));
    }

    @Test
    void deleteUser_ShouldReturnSuccessMessage() throws Exception {
        Integer userId = 1;

        when(userService.deleteUser(userId)).thenReturn(true);

        mockMvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().string("Delete successful"));

        verify(userService, times(1)).deleteUser(userId);
    }

    // 其他测试方法（如 resetPassword, assignRoles, enableUser, disableUser）可参照以上方式编写
}