package com.w2m.starshipregistry.integration;

import com.intuit.karate.junit5.Karate;

public class IntegrationTestRunner {

    @Karate.Test
    Karate testStarships() {
        return Karate.run("classpath:karate").tags("~@ignore");
    }
    
}