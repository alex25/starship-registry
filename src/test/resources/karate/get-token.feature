Feature: Get Keycloak Access Token

Background:
  * def keycloakHost = karate.properties['KEYCLOAK_HOST'] || 'localhost'
  * def keycloakPort = karate.properties['KEYCLOAK_PORT'] || '8080'

Scenario: Fetch access token
  Given url 'http://' + keycloakHost + ':' + keycloakPort + '/realms/starship-registry/protocol/openid-connect/token'
  And form field grant_type = 'password'
  And form field client_id = 'api-gateway'
  And form field username = 'user'
  And form field password = 'user123'
  And form field client_secret = 'client_secret'
  When method post
  Then status 200
  And match response.token_type == 'Bearer'
  * def accessToken = response.access_token
  * print 'Access Token:', accessToken