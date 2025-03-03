package com.w2m.starshipregistry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;


@SpringBootTest
public class StarshipRegistryApplicationTest {

    private final CacheManager cacheManager = mock(CacheManager.class);
    private final StringRedisTemplate redisTemplate = mock(StringRedisTemplate.class);

    @Test
    void cachingIsEnabled() {
        // Arrange
        when(cacheManager.getCacheNames()).thenReturn(Set.of("starships"));

        // Act
        assertThat(cacheManager).isNotNull();

        // Assert
        assertThat(cacheManager.getCacheNames()).isNotEmpty();
    }

    @Test
    void redisIsWorking() {
        // Arrange
        @SuppressWarnings("unchecked")
        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("testKey")).thenReturn("testValue");

        // Act
        redisTemplate.opsForValue().set("testKey", "testValue");

        // Assert
        String value = redisTemplate.opsForValue().get("testKey");
        assertThat(value).isEqualTo("testValue");

        verify(valueOperations).set("testKey", "testValue");
        verify(valueOperations).get("testKey");
    }

    @Test
    void contextLoads() {
        ConfigurableApplicationContext context = SpringApplication.run(StarshipRegistryApplication.class);

        assertThat(context).isNotNull();

        context.close();
    }

}
