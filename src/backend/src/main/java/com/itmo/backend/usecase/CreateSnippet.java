package com.itmo.backend.usecase;

import com.itmo.backend.domain.SnippetRequestBody;
import com.itmo.backend.service.SnippetService;
import org.springframework.beans.factory.annotation.Autowired;

public class CreateSnippet {
    @Autowired
    private SnippetService snippetService;

    public String create(SnippetRequestBody snippetData) {
        return snippetService.createSnippet(snippetData).getId();
    }
}
