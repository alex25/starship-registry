Feature: Modify Starship

  Background:
    * url 'http://localhost:8081/starship-registry/v1/'

  Scenario: Successfully modify an existing starship
    Given path 'starships/1'
    And request { "name": "Millennium Falcon 2", "movieId": 1 }
    When method put
    Then status 200
    And match response == 
"""
{
  "id": 1,
  "name": "Millennium Falcon 2",
  "movie": {
    "id": 1,
    "title": "Star Wars: A New Hope",
    "releaseYear": 1977,
    "isTvSeries": false
  }
}
"""
  Scenario: Attempt to modify a starship with a duplicate name
    Given path 'starships/2'
    And request { "name": "Millennium Falcon 2", "movieId": 1 }
    When method put
    Then status 409
    And match response ==  {"type":"about:blank","title":"Already exists","status":409,"detail":"Starship with ID 2 with the same name already exists","instance":"/starship-registry/v1/starships/2","id":2}

  Scenario: Attempt to modify a non-existent starship
    Given path 'starships/999'
    And request { "name": "Unknown Ship", "movieId": 100  }
    When method put
    Then status 404
    And match response == {"type":"about:blank","title":"Starship Not Found","status":404,"detail":"Starship with ID 999 not found","instance":"/starship-registry/v1/starships/999","id":999}

