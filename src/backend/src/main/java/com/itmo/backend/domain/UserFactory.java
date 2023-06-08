package com.itmo.backend.domain;

import com.itmo.backend.encoder.IPasswordEncoder;
public class UserFactory implements IUserFactory {
    private final IPasswordEncoder passwordEncoder;
    public UserFactory(IPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public User create(String login, String password) {
        return new User(login, passwordEncoder.hashPassword(password));
    }

}
