package com.itmo.backend;

import com.itmo.backend.domain.User;
import com.itmo.backend.encoder.PasswordEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordEncoderTests extends BaseTest {

    private final BCryptPasswordEncoder coreEncoder = new BCryptPasswordEncoder(10);

    @Test
    public void testHashPassword() {
        // Arrange
        PasswordEncoder passwordEncoder = new PasswordEncoder(coreEncoder);
        String password = "password123";

        // Act
        String hashedPassword = passwordEncoder.hashPassword(password);

        // Assert
        assertNotNull(hashedPassword);
        assertNotEquals(password, hashedPassword);
        assertTrue(coreEncoder.matches(password, hashedPassword));
    }

    @Test
    public void testIsCorrectPassword() {
        // Arrange
        PasswordEncoder passwordEncoder = new PasswordEncoder(coreEncoder);
        String password = "password123";
        User user = new User("testUser", coreEncoder.encode(password));

        // Act
        boolean isCorrect = passwordEncoder.isCorrectPassword(user, password);

        // Assert
        assertTrue(isCorrect);
    }
}
