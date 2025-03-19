package com.w2m.starshipregistry.integration;

import com.intuit.karate.junit5.Karate;

public class KarateTestRunner {

    @Karate.Test
    Karate karateTests() {
        return Karate.run("classpath:karate").tags("~@ignore");
    }
    
}