package com.itmo.backend.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
public class User implements IUser {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String Id;
    @Column(unique = true)
    private String login;
    private String passwordHash;
    @OneToMany(mappedBy = "user")
    private List<Snippet> snippets;

    public User() {}

    public User(String login, String passwordHash) {
        this.login = login;
        this.passwordHash = passwordHash;
    }

    @Override
    public String getId() {
        return Id;
    }

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public String getHash() {
        return passwordHash;
    }

}
