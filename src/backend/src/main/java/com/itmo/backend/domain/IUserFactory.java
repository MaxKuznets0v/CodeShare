package com.itmo.backend.domain;

public interface IUserFactory {
    public IUser create(String login, String password);
}
