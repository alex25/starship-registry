Feature: Find Starship

  Background:
    * url 'http://localhost:8080'

  Scenario: Successfully find a starship by ID
    Given path 'starships/1'
    When method get
    Then status 200
    And match response == { id: 1, name: "Millennium Falcon", movieTitle: "Star Wars" }

  Scenario: Attempt to find a non-existent starship by ID
    Given path 'starships/999'
    When method get
    Then status 404
    And match response == { error: "Starship with ID 999 not found" }

  Scenario: Search for starships by name
    Given path 'starships/search'
    And param name = 'Falcon'
    When method get
    Then status 200
    And match response == [{ id: 1, name: "Millennium Falcon", movieTitle: "Star Wars" }]

  Scenario: Search for starships by name with no matches
    Given path 'starships/search'
    And param name = 'Unknown'
    When method get
    Then status 200
    And match response == []

  Scenario: Retrieve all starships with pagination
    Given path 'starships'
    And param page = 0
    And param size = 2
    When method get
    * print response 
    Then status 200
    And match response == 
    """
    {
      content: [
        { id: 1, name: "Millennium Falcon", movieTitle: "Star Wars" },
        { id: 2, name: "USS Enterprise", movieTitle: "Star Trek" }
      ],
      totalPages: 2,
      totalElements: 3,
      number: 0,
      size: 2
    }
    """
  Scenario: Retrieve all starships with an empty result set
    Given path 'starships'
    When method get
    Then status 200
    And match response == { content: [], totalPages: 0, totalElements: 0, number: 0, size: 10 }