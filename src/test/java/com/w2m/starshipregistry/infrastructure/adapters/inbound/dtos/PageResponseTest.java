package com.w2m.starshipregistry.infrastructure.adapters.inbound.dtos;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

@ExtendWith(MockitoExtension.class)
public class PageResponseTest {

    @Mock
    private Page<String> mockPage;

    @Test
    void fromPageReturnsCorrectPageResponse() {
        // Arrange
        List<String> content = List.of("item1", "item2");
        int totalPages = 5;
        long totalElements = 10L;
        int pageNumber = 2;
        int pageSize = 3;

        when(mockPage.getContent()).thenReturn(content);
        when(mockPage.getTotalPages()).thenReturn(totalPages);
        when(mockPage.getTotalElements()).thenReturn(totalElements);
        when(mockPage.getNumber()).thenReturn(pageNumber);
        when(mockPage.getSize()).thenReturn(pageSize);

        // Act
        PageResponse<String> response = PageResponse.from(mockPage);

        // Assert
        assertNotNull(response);
        assertEquals(content, response.content());
        assertEquals(totalPages, response.totalPages());
        assertEquals(totalElements, response.totalElements());
        assertEquals(pageNumber, response.number());
        assertEquals(pageSize, response.size());
    }

    @Test
    void fromPageHandlesEmptyPage() {
        // Arrange
        List<String> emptyContent = List.of();
        int totalPages = 0;
        long totalElements = 0L;
        int pageNumber = 0;
        int pageSize = 10;

        when(mockPage.getContent()).thenReturn(emptyContent);
        when(mockPage.getTotalPages()).thenReturn(totalPages);
        when(mockPage.getTotalElements()).thenReturn(totalElements);
        when(mockPage.getNumber()).thenReturn(pageNumber);
        when(mockPage.getSize()).thenReturn(pageSize);

        // Act
        PageResponse<String> response = PageResponse.from(mockPage);

        // Assert
        assertNotNull(response);
        assertTrue(response.content().isEmpty());
        assertEquals(totalPages, response.totalPages());
        assertEquals(totalElements, response.totalElements());
        assertEquals(pageNumber, response.number());
        assertEquals(pageSize, response.size());
    }
}