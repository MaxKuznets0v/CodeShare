package com.itmo.backend.controller;

import com.itmo.backend.domain.ISnippet;
import com.itmo.backend.domain.SnippetRequestBody;
import com.itmo.backend.ui.SnippetView;
import com.itmo.backend.usecase.CreateSnippet;
import com.itmo.backend.usecase.GetSnippetById;
import com.mysql.cj.exceptions.WrongArgumentException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("snippet")
public class SnippetController {
    private final CreateSnippet createSnippet;
    private final GetSnippetById getSnippetById;
    public SnippetController(CreateSnippet createSnippet, GetSnippetById getSnippetById) {
        this.createSnippet = createSnippet;
        this.getSnippetById = getSnippetById;
    }

    @GetMapping("/{id}")
    public String index(@PathVariable("id") String id, Model model) {
        try {
            ISnippet res = getSnippetById.get(id);
            return SnippetView.showSnippet(res, model);
        } catch (WrongArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/create")
    @ResponseBody
    public String create(@RequestBody SnippetRequestBody snippetData) {
        return createSnippet.create(snippetData);
    }
}
