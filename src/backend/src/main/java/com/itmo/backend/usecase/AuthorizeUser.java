package com.itmo.backend.usecase;

import com.itmo.backend.domain.UserRequestBody;
import com.itmo.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthorizeUser {
    @Autowired
    private UserService userService;

    public String authorize(UserRequestBody userData) {
        return userService.authorizeUser(userData);
    }
}
