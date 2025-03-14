Feature: Find Starship

  Background:
    * url 'http://localhost:8081/starship-registry/v1/'

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
    And param name = 'slave'
    When method get
    Then status 200
    And match response == [{"id":4,"name":"Slave I","movie":{"id":2,"title":"The Empire Strikes Back","releaseYear":1980,"isTvSeries":false}}]


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


  Scenario: Get token of Keycloak and use to find starship by ID
    # Step 1: Get token of Keycloak
    Given url 'http://localhost:8080/realms/starship-registry/protocol/openid-connect/token'
    And form field grant_type = 'password'
    And form field client_id = 'api-gateway'
    And form field username = 'user'
    And form field password = 'user123'
    And form field client_secret = 'client_secret'
    When method post
    Then status 200
    And match response.token_type == 'Bearer'
    * def accessToken = response.access_token

* def hostApi = 'http://localhost:8888/api/starship-registry/v1/'

 # Step 2: Use token to find starship by ID
    Given url hostApi + '/starships/2'
    And header Authorization = 'Bearer ' + accessToken
    When method get
    Then status 200
    And match response contains { id: '#number', name: '#string' } 
