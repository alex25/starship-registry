package com.w2m.starshipregistry.core.exceptions;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class StarshipDuplicatedExceptionTest {

    @Test
    void defaultConstructor() {
        StarshipDuplicatedException exception = new StarshipDuplicatedException();
        
        assertEquals("Starship with the same name already exists", exception.getMessage());
        assertEquals(0L, exception.getId());
    }

    @Test
    void constructorWithId() {
        Long id = 123L;
        StarshipDuplicatedException exception = new StarshipDuplicatedException(id);
        
        assertEquals("Starship with ID 123 with the same name already exists", exception.getMessage());
        assertEquals(id, exception.getId());
    }

    @Test
    void constructorWithNegativeId() {
        Long id = -456L;
        StarshipDuplicatedException exception = new StarshipDuplicatedException(id);
        
        assertEquals("Starship with ID -456 with the same name already exists", exception.getMessage());
        assertEquals(id, exception.getId());
    }

    @Test
    void inheritsFromStarshipConflictException() {
        StarshipDuplicatedException exception = new StarshipDuplicatedException(789L);
        assertTrue(exception instanceof StarshipConflictException);
    }
}
