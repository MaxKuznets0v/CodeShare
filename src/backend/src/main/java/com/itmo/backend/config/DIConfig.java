package com.itmo.backend.config;

import com.itmo.backend.domain.ISnippetFactory;
import com.itmo.backend.domain.IUserFactory;
import com.itmo.backend.domain.SnippetFactory;
import com.itmo.backend.domain.UserFactory;
import com.itmo.backend.encoder.IPasswordEncoder;
import com.itmo.backend.encoder.PasswordEncoder;
import com.itmo.backend.usecase.AuthorizeUser;
import com.itmo.backend.usecase.CreateSnippet;
import com.itmo.backend.usecase.CreateUser;
import com.itmo.backend.usecase.GetSnippetById;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DIConfig {
    @Bean
    public ISnippetFactory createSnippetFactory() {
        return new SnippetFactory();
    }
    @Bean
    public BCryptPasswordEncoder createBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
    @Bean
    public IPasswordEncoder createPasswordEncoder() {
        return new PasswordEncoder(createBCryptPasswordEncoder());
    }
    @Bean
    public IUserFactory createUserFactory() {
        return new UserFactory(createPasswordEncoder());
    }
    @Bean
    public AuthorizeUser createAuthorizeUser() {
        return new AuthorizeUser();
    }
    @Bean
    public CreateUser createCreateUser() {
        return new CreateUser();
    }
    @Bean
    public CreateSnippet createCreateSnippet() {
        return new CreateSnippet();
    }
    @Bean
    public GetSnippetById createGetSnippetById() {
        return new GetSnippetById();
    }
}
