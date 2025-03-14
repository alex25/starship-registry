package com.w2m.apigateway.config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class AppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Bean
    public String logIssuerUri(Environment env) {
        String issuerUrl = env.getProperty("spring.security.oauth2.resourceserver.jwt.issuer-uri");
        String jwkSetUrl = env.getProperty("spring.security.oauth2.resourceserver.jwt.jwk-set-uri");
        String backUrl = env.getProperty("spring.cloud.gateway.routes.uri");

        logger.info("Configured issuer-url: {}", issuerUrl);
        logger.info("Configured jwk-set-url: {}", jwkSetUrl);
        logger.info("Configured back-url: {}", backUrl);

        return issuerUrl; 
    }
}