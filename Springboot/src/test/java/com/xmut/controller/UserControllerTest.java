package com.xmut.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmut.entity.User;
import com.xmut.service.UserService;
import com.xmut.util.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
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
        // 配置ResponseCharacterEncoding为UTF-8
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .characterEncoding("UTF-8")
                .build();
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

    @Test
    public void testResetPassword() throws Exception {
        when(userService.resetPassword(1, "newPassword")).thenReturn(true);
        mockMvc.perform(post("/users/resetPassword")
                .param("userId", "1")
                .param("newPassword", "newPassword"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("密码重置成功"));
    }

    @Test
    public void testAssignRoles() throws Exception {
        when(userService.assignRoles(1, List.of(1, 2, 3))).thenReturn(true);
        mockMvc.perform(post("/users/assignRoles")
                .param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[1, 2, 3]"))
                .andExpect(status().isOk())
                .andExpect(content().string("Roles assigned successfully"));
    }

    @Test
    public void testEnableUser() throws Exception {
        when(userService.enableUser(1)).thenReturn(true);
        mockMvc.perform(post("/users/enable/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User enabled"));
    }

    @Test
    public void testDisableUser() throws Exception {
        when(userService.disableUser(1)).thenReturn(true);
        mockMvc.perform(post("/users/disable/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User disabled"));
    }

    @Test
    public void testRegister() throws Exception {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        
        // 修正mock方法调用，确保使用正确的参数
        when(userService.isUsernameExists("username")).thenReturn(false);
        when(userService.register(any(User.class))).thenReturn(true);
        
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"username\", \"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("注册成功"));
    }

    @Test
    public void testGetCurrentUser() throws Exception {
        // 由于SecurityUtils是静态类，需要使用PowerMockito来mock
        // 这里我们修改测试策略，通过mock UserService来测试
        User user = new User();
        user.setUsername("username");
        
        // Mock SecurityUtils.getCurrentUser() 的返回值
        try (MockedStatic<SecurityUtils> mockedStatic = mockStatic(SecurityUtils.class)) {
            mockedStatic.when(SecurityUtils::getCurrentUser).thenReturn(user);
            
            mockMvc.perform(get("/users/current"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.data.username").value("username"));
        }
    }

    @Test
    public void testChangePassword() throws Exception {
        when(userService.changePassword("oldPassword", "newPassword")).thenReturn(true);
        mockMvc.perform(post("/users/changePassword")
                .param("oldPassword", "oldPassword")
                .param("newPassword", "newPassword"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("密码修改成功"));
    }

    @Test
    public void testGetUserWarehouses() throws Exception {
        when(userService.getUserWarehouses(1)).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/users/1/warehouses"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testSetUserWarehouses() throws Exception {
        when(userService.setUserWarehouses(1, Arrays.asList(1, 2, 3))).thenReturn(true);
        
        mockMvc.perform(post("/users/1/warehouses")
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[1, 2, 3]"))
                .andExpect(status().isOk())
                .andExpect(content().string("权限设置成功"));
    }
}