package com.itmo.backend.repository;

import com.itmo.backend.domain.Snippet;
import org.springframework.data.repository.CrudRepository;

public interface SnippetRepository extends CrudRepository<Snippet, String> {
}
