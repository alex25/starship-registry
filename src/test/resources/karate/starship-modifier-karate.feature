Feature: Modify Starship

  Background:
    * url 'http://localhost:8080' # Base URL of the application

  Scenario: Successfully modify an existing starship
    Given path 'starships/1'
    And request { "name": "Millennium Falcon 2", "movieTitle": "The Last Jedi" }
    When method put
    Then status 200
    And match response == { id: 1, name: "Millennium Falcon 2", movieTitle: "The Last Jedi" }

  Scenario: Attempt to modify a starship with a duplicate name
    Given path 'starships/2'
    And request { "name": "Millennium Falcon", "movieTitle": "Star Wars" }
    When method put
    Then status 409
    And match response == { error: "Starship with the same name already exists" }

  Scenario: Attempt to modify a non-existent starship
    Given path 'starships/999'
    And request { "name": "Unknown Ship", "movieTitle": "Unknown Movie" }
    When method put
    Then status 404
    And match response == { error: "Starship with ID 999 not found" }

  Scenario: Clear cache after modifying a starship
    Given path 'starships/1'
    And request { "name": "Millennium Falcon 3", "movieTitle": "Rogue One" }
    When method put
    Then status 200
    And match response == { id: 1, name: "Millennium Falcon 3", movieTitle: "Rogue One" }
    # Verify cache is cleared by checking subsequent requests
    And path 'starships/1'
    When method get
    Then status 200
    And match response == { id: 1, name: "Millennium Falcon 3", movieTitle: "Rogue One" }