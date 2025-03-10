package com.w2m.starshipregistry.core.exceptions;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class StarshipNotFoundExceptionTest {

    @Test
    void exceptionMessageAndId() {
        Long id = 123L;
        StarshipNotFoundException exception = new StarshipNotFoundException(id);
        
        assertEquals("Starship with ID 123 not found", exception.getMessage());
        assertEquals(id, exception.getId());
    }

    @Test
    void exceptionInheritsRuntimeException() {
        StarshipNotFoundException exception = new StarshipNotFoundException(456L);
        assertTrue(exception instanceof RuntimeException);
    }

}