Feature: Find Starship

  Background:
    * def apiGatewayHost = karate.properties['APIGATEWAY_HOST'] || 'localhost'
    * def apiGatewayPort = karate.properties['APIGATEWAY_PORT'] || '8888'
    * def keycloakHost = karate.properties['KEYCLOAK_HOST'] || 'localhost'
    * def keycloakPort = karate.properties['KEYCLOAK_PORT'] || '8080'
    * def appHost = karate.properties['APP_HOST'] || 'localhost'
    * def appPort = karate.properties['APP_PORT'] || '8081'
    * url 'http://' + appHost + ':' + appPort +'/starship-registry/v1/'
    * def hostApi = 'http://' + apiGatewayHost + ':' + apiGatewayPort + '/api/starship-registry/v1/'
    * def accessToken = karate.callSingle('classpath:karate/get-token.feature').accessToken

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

 # Step 2: Use token to find starship by ID
    Given url hostApi + '/starships/2'
    And header Authorization = 'Bearer ' + accessToken
    When method get
    Then status 200
    And match response contains { id: '#number', name: '#string' } 


  Scenario: Successfully find a starship by ID with token
    Given url hostApi + '/starships/2'
    And header Authorization = 'Bearer ' + accessToken
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

Scenario: Attempt to find a non-existent starship by ID with token
  Given url hostApi + 'starships/999'
  And header Authorization = 'Bearer ' + accessToken
  When method get
  Then status 404
  And match response ==  {"type":"about:blank","title":"Starship Not Found","status":404,"detail":"Starship with ID 999 not found","instance":"/starship-registry/v1/starships/999","id":999}

Scenario: Search for starships by name with token
  Given url hostApi + 'starships/search'
  And header Authorization = 'Bearer ' + accessToken
  And param name = 'slave'
  When method get
  Then status 200
  And match response == [{"id":4,"name":"Slave I","movie":{"id":2,"title":"The Empire Strikes Back","releaseYear":1980,"isTvSeries":false}}]

Scenario: Search for starships by name with no matches with token
  Given url hostApi + 'starships/search'
  And header Authorization = 'Bearer ' + accessToken
  And param name = 'Unknown'
  When method get
  Then status 200
  And match response == []

Scenario: Retrieve all starships with pagination with token
  Given url hostApi + 'starships'
  And header Authorization = 'Bearer ' + accessToken
  And param page = 1
  And param size = 2
  When method get
  * print response 
  Then status 200
