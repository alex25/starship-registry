Feature: Find Starship

  Background:
    * url 'http://localhost:8080/starship-registry/v1/'

  Scenario: Successfully find a starship by ID
    Given path 'starships/2'
    When method get
    Then status 200
    And match response ==
"""
{
  "id": 2,
  "name": "X-Wing",
  "movie": {
    "id": 1,
    "title": "Star Wars: A New Hope",
    "releaseYear": 1977,
    "isTvSeries": false
  }
}
"""

  Scenario: Attempt to find a non-existent starship by ID
    Given path 'starships/999'
    When method get
    Then status 404
    And match response ==  {"type":"about:blank","title":"Starship Not Found","status":404,"detail":"Starship with ID 999 not found","instance":"/starship-registry/v1/starships/999","id":999}

  Scenario: Search for starships by name
    Given path 'starships/search'
    And param name = 'falcon'
    When method get
    Then status 200
    And match response == 
"""
[
  {
    "id": 1,
    "name": "Millennium Falcon",
    "movie": {
      "id": 1,
      "title": "Star Wars: A New Hope",
      "releaseYear": 1977,
      "isTvSeries": false
    }
  }
]
"""

  Scenario: Search for starships by name with no matches
    Given path 'starships/search'
    And param name = 'Unknown'
    When method get
    Then status 200
    And match response == []

  Scenario: Retrieve all starships with pagination
    Given path 'starships'
    And param page = 1
    And param size = 2
    When method get
    * print response 
    Then status 200
    And match response == 
"""
{
  "content": [
    {
      "id": 3,
      "name": "TIE Fighter",
      "movie": {
        "id": 2,
        "title": "The Empire Strikes Back",
        "releaseYear": 1980,
        "isTvSeries": false
      }
    },
    {
      "id": 4,
      "name": "Slave I",
      "movie": {
        "id": 2,
        "title": "The Empire Strikes Back",
        "releaseYear": 1980,
        "isTvSeries": false
      }
    }
  ],
  "totalPages": 3,
  "totalElements": 5,
  "number": 1,
  "size": 2
}
"""
