Feature: Add New Starship

  Background:
    * url 'http://localhost:8080/starship-registry/v1/' 


  Scenario: Successfully add a new starship
    Given path '/starships'
    And request 
"""
{
  "name": "Millennium Falcon 4",
  "movie": {
    "title": "Star Wars: A New Hope 4",
    "releaseYear": 1977,
    "isTvSeries": false
  }
}
"""
    When method POST
    Then status 201
    And match response contains { id: '#number', name: "Enterprise III", movieTitle: "Star Wars" }
    # Verify cache is cleared (mock or check logs)
    * print 'Cache should be cleared for "starshipsByName", "starshipById", and "allStarships"'

  Scenario: Attempt to add a starship with a duplicate name
    Given path '/starships'
    And request { name: "X-Wing", movieTitle: "Star Wars" }
    When method POST
    Then status 409
    And match response == {"type":"about:blank","title":"Already exists","status":409,"detail":"Starship with ID null with the same name already exists","instance":"/starship-registry/v1/starships","id":null}

  Scenario: Handle database constraint violations when adding a starship
    Given path '/starships'
    And request { name: "InvalidShip", movieTitle: "InvalidMovie" }
    When method POST
    Then status 500
    And match response == { message: "Database constraint violation" }

  Scenario: Clear cache after adding a new starship
    Given path '/starships'
    And request { name: "Enterprise IV", movieTitle: "Rogue One" }
    When method POST
    Then status 201
    And match response contains { id: '#number', name: "Enterprise IV", movieTitle: "Rogue One" }
    # Mock or verify cache clearing logic
    * print 'Cache should be cleared for "starshipsByName", "starshipById", and "allStarships"'