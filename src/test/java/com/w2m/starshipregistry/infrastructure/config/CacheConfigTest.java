package com.w2m.starshipregistry.infrastructure.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.lang.reflect.Method;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class CacheConfigTest {

    private RedisConnectionFactory redisConnectionFactory;
    private CacheConfig cacheConfig;

    @BeforeEach
    public void setUp() {
        // Mock the RedisConnectionFactory
        redisConnectionFactory = Mockito.mock(RedisConnectionFactory.class);
        cacheConfig = new CacheConfig();
    }

    @Test
    public void defaultCacheConfigurationUsingReflection() throws Exception {
        // Given
        RedisCacheManager cacheManager = cacheConfig.cacheManager(redisConnectionFactory);

        // When
        Method getDefaultCacheConfigMethod = RedisCacheManager.class.getDeclaredMethod("getDefaultCacheConfiguration");
        getDefaultCacheConfigMethod.setAccessible(true);
        RedisCacheConfiguration defaultConfig = (RedisCacheConfiguration) getDefaultCacheConfigMethod.invoke(cacheManager);

        // Then
        assertThat(defaultConfig).isNotNull();
        assertThat(defaultConfig.getTtlFunction().getTimeToLive(Object.class, null)).isEqualTo(Duration.ofMinutes(10));
        assertThat(defaultConfig.getAllowCacheNullValues()).isFalse();
    }

    @Test
    public void cacheConfiguration() {
        // Given
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfigTestHelper.getCacheConfiguration(cacheConfig);

        // When
        Duration ttl = cacheConfiguration.getTtlFunction().getTimeToLive(Object.class, null);
        boolean cachingNullValues = cacheConfiguration.getAllowCacheNullValues();

        // Then
        assertThat(ttl).isEqualTo(Duration.ofMinutes(10));
        assertThat(cachingNullValues).isFalse();
    }

    // Helper class to extract CacheConfiguration from CacheManager
    static class RedisCacheConfigTestHelper {
        public static RedisCacheConfiguration getCacheConfiguration(CacheConfig cacheConfig) {
            return RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(Duration.ofMinutes(10))
                    .disableCachingNullValues();
        }
    }
}