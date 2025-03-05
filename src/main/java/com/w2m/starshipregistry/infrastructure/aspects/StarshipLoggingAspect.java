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

    private static final Logger logger = LoggerFactory.getLogger(StarshipLoggingAspect.class);

    // Define el punto de corte: métodos que buscan una nave por ID
    @Pointcut("execution(* com.w2m.starshipregistry.core.ports.inbound.FindByIdStarshipPort.execute(..)) && args(id)")
    public void findByIdStarshipPortExecution(Long id) {
        // Punto de corte vacío
    }

    // Antes de ejecutar el método, verifica si el ID es negativo
    @Before("findByIdStarshipPortExecution(id)")
    public void logNegativeId(Long id) {
        if (id < 0) {
            logger.warn("Se ha solicitado una nave con un ID negativo: {}", id);
        }
    }
}