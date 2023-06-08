package com.itmo.backend.domain;

public interface ISnippetFactory {
    public ISnippet create(IUser author, String content);
}
