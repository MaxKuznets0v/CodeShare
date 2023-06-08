package com.itmo.backend.service;

import com.itmo.backend.domain.*;
import com.itmo.backend.repository.SnippetRepository;
import com.mysql.cj.exceptions.WrongArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SnippetService {
    @Value("${default.user.login}")
    private String defaultUsername;
    @Autowired
    private SnippetRepository snippetRepository;
    private final UserService userService;
    private final ISnippetFactory snippetFactory;

    public SnippetService(UserService userService, ISnippetFactory snippetFactory) {
        this.userService = userService;
        this.snippetFactory = snippetFactory;
    }

    public Snippet getSnippetById(String id) throws WrongArgumentException {
        Optional<Snippet> res = snippetRepository.findById(id);
        if (res.isEmpty()) {
            throw new WrongArgumentException("Snippet with given ID is not found!");
        }
        return res.get();
    }

    public Snippet createSnippet(SnippetRequestBody snippetData) {
        return snippetRepository.save(parseSnippet(snippetData));
    }

    private Snippet parseSnippet(SnippetRequestBody snippetData) {
        User author;
        try {
            author = userService.getUserById(snippetData.getUserId());
        } catch (WrongArgumentException e) {
            author =  userService.getUserByLogin(defaultUsername);
        }
        return (Snippet) snippetFactory.create(author, snippetData.getSnippet());
    }
}
