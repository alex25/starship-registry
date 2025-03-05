Feature: Remove Starship

  Background:
    * url 'http://localhost:8080/starship-registry/v1/' 

  Scenario: Successfully remove an existing starship
    Given path 'starships/5'
    When method delete
    Then status 204

  Scenario: Attempt to remove a non-existent starship
    Given path 'starships/999'
    When method delete
    Then status 404
    And match response == { error: "Starship with ID 999 not found" }

  Scenario: Attempt to remove a starship with existing dependencies
    Given path 'starships/5'
    When method delete
    Then status 409
    And match response == { error: "Starship with ID 2 cannot be deleted due to existing dependencies" }

  Scenario: Clear cache after removing a starship
    Given path 'starships/5'
    When method delete
    Then status 204
    # Verify cache is cleared by checking subsequent requests
    And path 'starships/5'
    When method get
    Then status 404
    And match response == { error: "Starship with ID 1 not found" }