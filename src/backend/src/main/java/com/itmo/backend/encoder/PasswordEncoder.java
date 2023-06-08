package com.itmo.backend.encoder;

import com.itmo.backend.domain.IUser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder implements IPasswordEncoder {
    private final BCryptPasswordEncoder encoder;

    public PasswordEncoder(BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public String hashPassword(String password) {
        return encoder.encode(password);
    }

    @Override
    public boolean isCorrectPassword(IUser user, String password) {
        return encoder.matches(password, user.getHash());
    }
}
