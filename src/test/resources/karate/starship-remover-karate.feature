Feature: Remove Starship

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

  Scenario: Successfully remove an existing starship
    Given path '/starships'
    And request 
    """
    {
      "name": "DeletedStarship",
      "movieTitle": "Star Wars: A New Hope",
      "movieRelease": 1977,
      "isTvSeries": false
    }
    """
    When method POST
    Then status 201
    * def starshipId = response.id

    Given path 'starships/' + starshipId
    When method delete
    Then status 204

    Given path 'starships/' + starshipId
    When method get
    Then status 404

  Scenario: Attempt to remove a non-existent starship
    Given path 'starships/999'
    When method delete
    Then status 404
    And match response == {"type":"about:blank","title":"Starship Not Found","status":404,"detail":"Starship with ID 999 not found","instance":"/starship-registry/v1/starships/999","id":999}

Scenario: Successfully remove an existing starship with token
  Given url hostApi + '/starships'
  And header Authorization = 'Bearer ' + accessToken
  And request 
  """
  {
    "name": "DeletedStarship",
    "movieTitle": "Star Wars: A New Hope",
    "movieRelease": 1977,
    "isTvSeries": false
  }
  """
  When method POST
  Then status 201
  * def starshipId = response.id

  Given url hostApi + 'starships/' + starshipId
  And header Authorization = 'Bearer ' + accessToken
  When method delete
  Then status 204

  Given url hostApi + 'starships/' + starshipId
  And header Authorization = 'Bearer ' + accessToken
  When method get
  Then status 404

Scenario: Attempt to remove a non-existent starship with token
  Given url hostApi + 'starships/999'
  And header Authorization = 'Bearer ' + accessToken
  When method delete
  Then status 404
  And match response == {"type":"about:blank","title":"Starship Not Found","status":404,"detail":"Starship with ID 999 not found","instance":"/starship-registry/v1/starships/999","id":999}
