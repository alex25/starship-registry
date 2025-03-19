Feature: Send update to RabbitMQ queue 'starship-updates' ussing HTTP API

  Background:
    * url 'http://localhost:15672/api/exchanges/%2F/amq.default/publish'
    * def generateRandom = 
    """
    function() {
        return Math.round(Math.random() * 1000);
    }
    """
    * def name = "Millennium Falcon" + generateRandom();

Scenario: Publish update to the default exchange
  Given def starshipData = { id: 1, name: "#(name)", movieId: 1 }
  And header Authorization = 'Basic YWRtaW46YWRtaW4xMjM='
  And header Content-Type = 'application/json'
  And def queueName = "starship-updates"
  And def payload =
    """
    {
      "properties": {},
      "routing_key": "#(queueName)",
      "payload": "#(JSON.stringify(starshipData))",
      "payload_encoding": "string"
    }
    """
  * print payload
  And request payload
  When method POST
  Then status 200
  And print response