package com.itmo.backend.domain;

public class SnippetFactory implements ISnippetFactory {
    @Override
    public Snippet create(IUser author, String content) {
        return new Snippet(author, content);
    }
}
