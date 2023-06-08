package com.itmo.backend.ui;

import com.itmo.backend.domain.ISnippet;
import org.springframework.ui.Model;

public class SnippetView {
    static public String showSnippet(ISnippet snippet, Model body) {
        body.addAttribute("content", snippet.getContent());
        body.addAttribute("author", snippet.getUser().getLogin());
        return "snippet";
    }
}
