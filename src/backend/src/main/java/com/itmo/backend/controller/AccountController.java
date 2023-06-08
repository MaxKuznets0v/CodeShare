package com.itmo.backend.controller;

import com.itmo.backend.domain.UserRequestBody;
import com.itmo.backend.usecase.AuthorizeUser;
import com.itmo.backend.usecase.CreateUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final AuthorizeUser authorizeUser;
    private final CreateUser createUser;
    public AccountController(AuthorizeUser authorizeUser, CreateUser createUser) {
        this.authorizeUser = authorizeUser;
        this.createUser = createUser;
    }

    @PostMapping("/login")
    public String login(@RequestBody UserRequestBody userData) {
        return authorizeUser.authorize(userData);
    }

    @PostMapping("/register")
    public void register(@RequestBody UserRequestBody userData) {
        createUser.create(userData);
    }
}
