package com.itmo.backend.config;

import com.itmo.backend.domain.UserRequestBody;
import com.itmo.backend.service.UserService;
import com.mysql.cj.exceptions.WrongArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private UserService userService;
    @Value("${default.user.login}")
    private String defaultUsername;

    @Value("${default.user.password}")
    private String defaultUserPassword;

    @Override
    public void run(String... args) {
        try {
            userService.getUserByLogin(defaultUsername);
        } catch (WrongArgumentException e) {
            UserRequestBody userData = new UserRequestBody(defaultUsername, defaultUserPassword);
            userService.createUser(userData);
        }
    }
}
