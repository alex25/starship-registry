package com.w2m.starshipregistry.infrastructure.aspects;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import com.w2m.starshipregistry.core.ports.inbound.FindByIdStarshipPort;

public class StarshipLoggingAspectTest {

    private FindByIdStarshipPort findByIdStarshipPort;
    private StarshipLoggingAspect starshipLoggingAspect;
    private Logger logger;

    @BeforeEach
    public void setUp() {
        // Create a mock for the logger
        logger = mock(Logger.class);

        // Create the aspect with the mocked logger
        starshipLoggingAspect = new StarshipLoggingAspect(logger);

        // Create the target object (mocked)
        findByIdStarshipPort = mock(FindByIdStarshipPort.class);

        // Create a proxy for the target object with the aspect
        AspectJProxyFactory proxyFactory = new AspectJProxyFactory(findByIdStarshipPort);
        proxyFactory.addAspect(starshipLoggingAspect);

        // Get the proxied object
        findByIdStarshipPort = proxyFactory.getProxy();
    }

    @Test
    public void logNegativeId() {
        // Given
        Long negativeId = -5L;

        // When
        findByIdStarshipPort.execute(negativeId);

        // Then
        verify(logger, times(1)).warn("Starship request with negative ID: {}", negativeId);
    }

    @Test
    public void logNonNegativeId() {
        // Given
        Long nonNegativeId = 10L;

        // When
        findByIdStarshipPort.execute(nonNegativeId);

        // Then
        verify(logger, never()).warn(anyString(), anyLong());
    }
}