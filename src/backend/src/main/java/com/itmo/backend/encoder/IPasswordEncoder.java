package com.itmo.backend.encoder;

import com.itmo.backend.domain.IUser;

public interface IPasswordEncoder {
    String hashPassword(String password);
    boolean isCorrectPassword(IUser user, String password);
}
