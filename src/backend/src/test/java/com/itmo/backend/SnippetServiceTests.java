package com.itmo.backend;

import com.itmo.backend.domain.Snippet;
import com.itmo.backend.domain.SnippetRequestBody;
import com.itmo.backend.domain.User;
import com.itmo.backend.repository.SnippetRepository;
import com.itmo.backend.service.SnippetService;
import com.itmo.backend.service.UserService;
import com.itmo.backend.domain.ISnippetFactory;
import com.mysql.cj.exceptions.WrongArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SnippetServiceTests extends BaseTest {

    @Mock
    private SnippetRepository snippetRepository;

    @Mock
    private UserService userService;

    @Mock
    private ISnippetFactory snippetFactory;

    @Value("${default.user.login}")
    private String defaultUsername;

    private SnippetService snippetService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        snippetService = new SnippetService(userService, snippetFactory);
        setPrivateField(snippetService, "snippetRepository", snippetRepository);
        setPrivateField(snippetService, "defaultUsername", defaultUsername);
    }

    @Test
    public void testGetSnippetById_ExistingId_ReturnsSnippet() throws WrongArgumentException {
        // Arrange
        String snippetId = "123";
        Snippet expectedSnippet = new Snippet();
        when(snippetRepository.findById(snippetId)).thenReturn(Optional.of(expectedSnippet));

        // Act
        Snippet result = snippetService.getSnippetById(snippetId);

        // Assert
        assertEquals(expectedSnippet, result);
    }

    @Test
    public void testGetSnippetById_NonExistingId_ThrowsWrongArgumentException() {
        // Arrange
        String snippetId = "123";
        when(snippetRepository.findById(snippetId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(WrongArgumentException.class, () -> snippetService.getSnippetById(snippetId));
    }

    @Test
    public void testCreateSnippet_ValidData_ReturnsCreatedSnippet() throws WrongArgumentException {
        // Arrange
        SnippetRequestBody snippetData = new SnippetRequestBody();
        User author = new User();
        Snippet createdSnippet = new Snippet();
        when(userService.getUserById(snippetData.getUserId())).thenReturn(author);
        when(snippetFactory.create(author, snippetData.getSnippet())).thenReturn(createdSnippet);
        when(snippetRepository.save(createdSnippet)).thenReturn(createdSnippet);

        // Act
        Snippet result = snippetService.createSnippet(snippetData);

        // Assert
        assertNotNull(result);
        assertEquals(createdSnippet, result);
        verify(snippetRepository, times(2)).save(createdSnippet);
    }

    @Test
    public void testCreateSnippet_InvalidUserId_ReturnsCreatedSnippetWithDefaultAuthor() throws WrongArgumentException {
        // Arrange
        SnippetRequestBody snippetData = new SnippetRequestBody();
        User defaultAuthor = new User();
        Snippet createdSnippet = new Snippet();
        when(userService.getUserById(snippetData.getUserId())).thenThrow(new WrongArgumentException(""));
        when(userService.getUserByLogin(defaultUsername)).thenReturn(defaultAuthor);
        when(snippetFactory.create(defaultAuthor, snippetData.getSnippet())).thenReturn(createdSnippet);
        when(snippetRepository.save(createdSnippet)).thenReturn(createdSnippet);

        // Act
        Snippet result = snippetService.createSnippet(snippetData);

        // Assert
        assertNotNull(result);
        assertEquals(createdSnippet, result);
        verify(snippetRepository, times(2)).save(createdSnippet);
    }

}
