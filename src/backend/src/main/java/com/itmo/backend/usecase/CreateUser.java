package com.itmo.backend.usecase;

import com.itmo.backend.domain.UserRequestBody;
import com.itmo.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class CreateUser {
    @Autowired
    private UserService userService;

    public void create(UserRequestBody userData) {
        userService.createUser(userData);
    }
}
