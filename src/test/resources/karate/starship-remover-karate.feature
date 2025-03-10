Feature: Remove Starship

  Background:
    * url 'http://localhost:8081/starship-registry/v1/' 

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

