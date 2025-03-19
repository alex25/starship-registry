Feature: get modifications from 'starship-modifications' into RabbitMQ

  Scenario: Get modifications from the queue 
    Given url 'http://localhost:15672/api/queues/%2F/starship-modifications/get'
    And header Authorization = 'Basic YWRtaW46YWRtaW4xMjM='
    And header Content-Type = 'application/json'
    And request
      """
      {
        "count": 1,
        "ackmode": "ack_requeue_false",
        "encoding": "auto"
      }
      """
    When method POST
    Then status 200
    And print 'Respuesta recibida:', response