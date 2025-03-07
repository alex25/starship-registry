Feature: Add New Starship

  Background:
    * url 'http://localhost:8081/starship-registry/v1/' 
    * def generateUUID = 
  """
  function() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
      var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
      return v.toString(16);
    });
  }
  """


Scenario: Successfully add a new starship with random name
    * def randomName = 'Starship_' + generateUUID()
    Given path '/starships'
    And request 
    """
    {
      "name": "#(randomName)",
      "movieTitle": "Star Wars: A New Hope",
      "movieRelease": 1977,
      "isTvSeries": false
    }
    """
    When method POST
    Then status 201
    And match response contains 
    """
    {
      id: #number,
      name: "#(randomName)",
      movie: {
        id: #number,
        title: "Star Wars: A New Hope",
        releaseYear: 1977,
        isTvSeries: false
      }
    }
    """
    # Verify cache is cleared (mock or check logs)
    * print 'Cache should be cleared for "starshipsByName", "starshipById", and "allStarships"'

  Scenario: Attempt to add a starship with a duplicate name
    Given path '/starships'
    And request 
    """
    {
      "name": "X-Wing",
      "movieTitle": "Star Wars: A New Hope",
      "movieRelease": 1977,
      "isTvSeries": false
    }
    """
    When method POST
    Then status 409
    And match response == {"type":"about:blank","title":"Already exists","status":409,"detail":"Starship with the same name already exists","instance":"/starship-registry/v1/starships","id":0}

  Scenario: Handle database constraint violations when adding a starship
    Given path '/starships'
    And request { "name": "InvalidShip", "movieTitle": "InvalidMovie" }
    When method POST
    Then status 409
