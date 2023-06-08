package com.itmo.backend.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Snippet implements ISnippet {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "content", columnDefinition = "text")
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Snippet() {}

    public Snippet(IUser author, String content) {
        this.content = content;
        user = (User) author;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public IUser getUser() {
        return user;
    }

    @Override
    public String getContent() {
        return content;
    }

}
