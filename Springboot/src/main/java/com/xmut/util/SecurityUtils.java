package com.xmut.util;

import com.xmut.entity.User;
import com.xmut.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
    
    public static User getCurrentUser() {
        Integer userId = UserContext.getUserId();
        if (userId == null) {
            return null;
        }
        return ApplicationContextProvider.getBean(UserService.class).getById(userId);
    }
}
