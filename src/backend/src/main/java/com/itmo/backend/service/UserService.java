package com.itmo.backend.service;

import com.itmo.backend.domain.User;
import com.itmo.backend.domain.IUserFactory;
import com.itmo.backend.domain.UserRequestBody;
import com.itmo.backend.encoder.IPasswordEncoder;
import com.itmo.backend.repository.UserRepository;
import com.mysql.cj.exceptions.WrongArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;
    private final IUserFactory userFactory;
    private final IPasswordEncoder encoder;

    public UserService(IUserFactory userFactory, IPasswordEncoder encoder) {
        this.userFactory = userFactory;
        this.encoder = encoder;
    }

    public User getUserById(String id) {
        if (id == null) {
            throw new WrongArgumentException("User id should not be null!");
        }
        Optional<User> res = repository.findById(id);
        if (res.isEmpty()) {
            throw new WrongArgumentException("User with given ID is not found!");
        }
        return res.get();
    }

    public User getUserByLogin(String login) {
        Optional<User> res = repository.findByLogin(login);
        if (res.isEmpty()) {
            throw new WrongArgumentException("User with given login is not found!");
        }
        return res.get();
    }

    public User createUser(UserRequestBody userData) {
        try {
            getUserByLogin(userData.getLogin());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");
        } catch (WrongArgumentException e) {
            User user = (User) userFactory.create(userData.getLogin(), userData.getPassword());
            return repository.save(user);
        }
    }

    public String authorizeUser(UserRequestBody userData) {
        try {
            User u = getUserByLogin(userData.getLogin());
            if (!encoder.isCorrectPassword(u, userData.getPassword())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Wrong credentials");
            }
            return u.getId();
        } catch (WrongArgumentException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Wrong credentials");
        }
    }
}
