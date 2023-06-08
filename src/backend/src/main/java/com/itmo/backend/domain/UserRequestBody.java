package com.itmo.backend.domain;

public class UserRequestBody {
    private final String login;
    private final String password;

    public UserRequestBody(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
