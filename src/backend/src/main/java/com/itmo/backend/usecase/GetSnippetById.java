package com.itmo.backend.usecase;

import com.itmo.backend.domain.ISnippet;
import com.itmo.backend.service.SnippetService;
import org.springframework.beans.factory.annotation.Autowired;

public class GetSnippetById {
    @Autowired
    private SnippetService snippetService;

    public ISnippet get(String id) {
        return snippetService.getSnippetById(id);
    }
}
