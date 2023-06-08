package com.itmo.backend;

import com.itmo.backend.domain.IUserFactory;
import com.itmo.backend.domain.User;
import com.itmo.backend.domain.UserRequestBody;
import com.itmo.backend.encoder.IPasswordEncoder;
import com.itmo.backend.repository.UserRepository;
import com.itmo.backend.service.UserService;
import com.mysql.cj.exceptions.WrongArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTests extends BaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private IUserFactory userFactory;

    @Mock
    private IPasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userFactory, passwordEncoder);
        setPrivateField(userService, "repository", userRepository);
    }

    @Test
    public void testGetUserById_ExistingId_ReturnsUser() throws WrongArgumentException {
        // Arrange
        String userId = "123";
        User expectedUser = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        // Act
        User result = userService.getUserById(userId);

        // Assert
        assertEquals(expectedUser, result);
    }

    @Test
    public void testGetUserById_NullId_ThrowsWrongArgumentException() {
        // Act & Assert
        assertThrows(WrongArgumentException.class, () -> userService.getUserById(null));
    }

    @Test
    public void testGetUserById_NonExistingId_ThrowsWrongArgumentException() {
        // Arrange
        String userId = "123";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(WrongArgumentException.class, () -> userService.getUserById(userId));
    }

    @Test
    public void testGetUserByLogin_ExistingLogin_ReturnsUser() throws WrongArgumentException {
        // Arrange
        String login = "testUser";
        User expectedUser = new User();
        when(userRepository.findByLogin(login)).thenReturn(Optional.of(expectedUser));

        // Act
        User result = userService.getUserByLogin(login);

        // Assert
        assertEquals(expectedUser, result);
    }

    @Test
    public void testGetUserByLogin_NonExistingLogin_ThrowsWrongArgumentException() {
        // Arrange
        String login = "testUser";
        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(WrongArgumentException.class, () -> userService.getUserByLogin(login));
    }

    @Test
    public void testCreateUser_ValidData_ReturnsCreatedUser() throws WrongArgumentException {
        // Arrange
        UserRequestBody userData = new UserRequestBody("testUser", "password");
        User expectedUser = new User();

        when(userFactory.create(userData.getLogin(), userData.getPassword())).thenReturn(expectedUser);
        when(userRepository.findByLogin(userData.getLogin())).thenReturn(Optional.empty());
        when(userRepository.save(expectedUser)).thenReturn(expectedUser);

        // Act
        User result = userService.createUser(userData);

        // Assert
        assertEquals(expectedUser, result);
    }

    @Test
    public void testCreateUser_ExistingUser_ThrowsResponseStatusException() {
        // Arrange
        UserRequestBody userData = new UserRequestBody("existingUser", "password");
        User existingUser = new User();

        when(userRepository.findByLogin(userData.getLogin())).thenReturn(Optional.of(existingUser));

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> userService.createUser(userData));
    }

    @Test
    public void testAuthorizeUser_CorrectCredentials_ReturnsUserId() throws WrongArgumentException {
        // Arrange
        UserRequestBody userData = new UserRequestBody("testUser", "password");
        User user = mock(User.class, withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS));
        String userId = "123";

        when(userRepository.findByLogin(userData.getLogin())).thenReturn(Optional.of(user));
        when(passwordEncoder.isCorrectPassword(user, userData.getPassword())).thenReturn(true);
        doReturn(userId).when(user).getId();

        // Act
        String result = userService.authorizeUser(userData);

        // Assert
        assertEquals(userId, result);
    }

    @Test
    public void testAuthorizeUser_WrongCredentials_ThrowsResponseStatusException() {
        // Arrange
        UserRequestBody userData = new UserRequestBody("testUser", "password");
        User user = new User();

        when(userRepository.findByLogin(userData.getLogin())).thenReturn(Optional.of(user));
        when(passwordEncoder.isCorrectPassword(user, userData.getPassword())).thenReturn(false);

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> userService.authorizeUser(userData));
    }

    @Test
    public void testAuthorizeUser_NonExistingUser_ThrowsResponseStatusException() {
        // Arrange
        UserRequestBody userData = new UserRequestBody("nonExistingUser", "password");

        when(userRepository.findByLogin(userData.getLogin())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> userService.authorizeUser(userData));
    }
}

