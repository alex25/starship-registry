package com.w2m.starshipregistry.core.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.jupiter.api.Test;

public class MovieDtoTest {

    @Test
    void constructorAndGetters() {
        Long id = 1L;
        String title = "Inception";
        Integer releaseYear = 2010;
        Boolean isTvSeries = false;

        MovieDto movieDto = new MovieDto(id, title, releaseYear, isTvSeries);

        assertEquals(id, movieDto.id());
        assertEquals(title, movieDto.title());
        assertEquals(releaseYear, movieDto.releaseYear());
        assertEquals(isTvSeries, movieDto.isTvSeries());
    }

    @Test
    void fromMovieParent() {
        MovieDtoNullable parent = mock(MovieDtoNullable.class);
        when(parent.id()).thenReturn(2L);
        when(parent.title()).thenReturn("The Matrix");
        when(parent.releaseYear()).thenReturn(1999);
        when(parent.isTvSeries()).thenReturn(true);

        MovieDtoNullable result = MovieDto.fromMovieParent(parent);

        assertEquals(parent.id(), result.id());
        assertEquals(parent.title(), result.title());
        assertEquals(parent.releaseYear(), result.releaseYear());
        assertEquals(parent.isTvSeries(), result.isTvSeries());
    }

    @Test
    void serializable() throws Exception {
        MovieDto original = new MovieDto(3L, "Interstellar", 2014, false);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(original);
        oos.flush();

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        MovieDto deserialized = (MovieDto) ois.readObject();

        assertEquals(original, deserialized);
    }

    @Test
    void equalsAndHashCode() {
        MovieDto dto1 = new MovieDto(4L, "Dune", 2021, false);
        MovieDto dto2 = new MovieDto(4L, "Dune", 2021, false);
        MovieDto dto3 = new MovieDto(5L, "Blade Runner", 1982, true);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1, dto3);
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    void testToString() {
        MovieDto dto = new MovieDto(6L, "The Prestige", 2006, false);
        String expected = "MovieDto[id=6, title=The Prestige, releaseYear=2006, isTvSeries=false]";
        assertEquals(expected, dto.toString());
    }
}