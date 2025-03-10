package com.w2m.starshipregistry.infrastructure.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class StarshipLoggingAspect {

    private final Logger logger;

    public StarshipLoggingAspect(Logger logger) {
        this.logger = logger;
    }

    public StarshipLoggingAspect() {
        this.logger = LoggerFactory.getLogger(StarshipLoggingAspect.class);
    }

    @Pointcut("execution(* com.w2m.starshipregistry.core.ports.inbound.FindByIdStarshipPort.execute(..)) && args(id)")
    public void findByIdStarshipPortExecution(Long id) {
    }

    @Before("findByIdStarshipPortExecution(id)")
    public void logNegativeId(Long id) {
        if (id < 0) {
            logger.warn("Starship request with negative ID: {}", id);
        }
    }
}