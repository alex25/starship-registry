package com.w2m.starshipregistry.infrastructure.adapters.outbound.starshipdata.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DependencyConflictExceptionTest {

    @Test
    public void testConstructorWithMessageAndCause() {
        // Given
        String message = "Dependency conflict detected.";
        Throwable cause = new IllegalArgumentException("Invalid dependency provided.");

        // When
        DependencyConflictException exception = new DependencyConflictException(message, cause);

        // Then
        assertNotNull(exception, "The exception should not be null.");
        assertEquals(message, exception.getMessage(), "The exception message should match the provided message.");
        assertEquals(cause, exception.getCause(), "The exception cause should match the provided cause.");
        assertTrue(exception instanceof RuntimeException, "DependencyConflictException should be a subclass of RuntimeException.");
    }

}