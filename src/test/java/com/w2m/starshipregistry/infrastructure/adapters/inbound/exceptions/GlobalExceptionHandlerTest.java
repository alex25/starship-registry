package com.w2m.starshipregistry.infrastructure.adapters.inbound.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.w2m.starshipregistry.core.exceptions.StarshipDependencyException;
import com.w2m.starshipregistry.core.exceptions.StarshipDuplicatedException;
import com.w2m.starshipregistry.core.exceptions.StarshipNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = mock(HttpServletRequest.class);
    }

    @SuppressWarnings("null")
    @Test
    void handleStarshipNotFoundExceptionReturnsNotFound() {
        Long id = 123L;
        StarshipNotFoundException ex = new StarshipNotFoundException(id);

        ResponseEntity<ProblemDetail> response = handler.handleStarshipNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ProblemDetail problemDetail = response.getBody();
        assertNotNull(problemDetail);
        assertEquals("Starship Not Found", problemDetail.getTitle());
        assertEquals("Starship with ID " + id + " not found", problemDetail.getDetail());
        assertEquals(id, problemDetail.getProperties().get("id"));
    }

    @Test
    void handleIllegalArgumentExceptionReturnsBadRequest() {
        String message = "Invalid argument";
        IllegalArgumentException ex = new IllegalArgumentException(message);

        ResponseEntity<ProblemDetail> response = handler.handleIllegalArgumentException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ProblemDetail problemDetail = response.getBody();
        assertNotNull(problemDetail);
        assertEquals("Bad Request", problemDetail.getTitle());
        assertEquals(message, problemDetail.getDetail());
    }

    @SuppressWarnings("null")
    @Test
    void handleStarshipDuplicatedExceptionReturnsConflict() {
        Long id = 456L;
        String message = "Starship with ID 456 with the same name already exists";
        StarshipDuplicatedException ex = new StarshipDuplicatedException(id);

        ResponseEntity<ProblemDetail> response = handler.handleStarshipDuplicatedException(ex, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        ProblemDetail problemDetail = response.getBody();
        assertNotNull(problemDetail);
        assertEquals("Already exists", problemDetail.getTitle());
        assertEquals(message, problemDetail.getDetail());
        assertEquals(id, problemDetail.getProperties().get("id"));
    }

    @Test
    void handleStarshipDependencyExceptionReturnsConflict() {
        String message = "Dependency exists";
        StarshipDependencyException ex = new StarshipDependencyException(message, new Exception());

        ResponseEntity<ProblemDetail> response = handler.handleStarshipDependencyException(ex, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        ProblemDetail problemDetail = response.getBody();
        assertNotNull(problemDetail);
        assertEquals("Conflict", problemDetail.getTitle());
        assertEquals(message, problemDetail.getDetail());
    }

    @Test
    void handleValidationExceptionsReturnsFieldErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldError("object", "field1", "message1"));
        fieldErrors.add(new FieldError("object", "field2", "message2"));
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);
        @SuppressWarnings("null")
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<Map<String, String>> response = handler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> errors = response.getBody();
        assertNotNull(errors);
        assertEquals(2, errors.size());
        assertEquals("message1", errors.get("field1"));
        assertEquals("message2", errors.get("field2"));
    }
}