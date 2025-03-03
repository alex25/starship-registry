Feature: Use case Find Starship
  As a user of the Starship Registry system
  I want to search for starships by different criteria
  So that I can retrieve relevant starship information

  Background:
    Given the Starship Registry application is running
    And the following starships exist in the system:
      | id  | name               | movieTitle     |
      | 1   | Millennium Falcon  | Star Wars      |
      | 2   | USS Enterprise     | Star Trek      |
      | 3   | X-Wing             | Star Wars      |

  Scenario: Successfully find a starship by ID
    Given I want to find the starship with ID "1"
    When I search for the starship by ID
    Then I should receive the following starship details:
      | id  | name               | movieTitle     |
      | 1   | Millennium Falcon  | Star Wars      |

  Scenario: Attempt to find a non-existent starship by ID
    Given I want to find the starship with ID "999"
    When I search for the starship by ID
    Then the system should return no results
    And I should receive a message stating "Starship with ID 999 not found"

  Scenario: Search for starships by name
    Given I want to search for starships with the name "falcon"
    When I search for starships by name
    Then I should receive the following starship details:
      | id  | name               | movieTitle     |
      | 1   | Millennium Falcon  | Star Wars      |

  Scenario: Search for starships by name with no matches
    Given I want to search for starships with the name "Unknown"
    When I search for starships by name
    Then the system should return no results
    And I should receive an empty list

  Scenario: Retrieve all starships with pagination
    Given I want to retrieve all starships
    When I request the first page with a page size of 2
    Then I should receive the following starship details:
      | id  | name               | movieTitle     |
      | 1   | Millennium Falcon  | Star Wars      |
      | 2   | USS Enterprise     | Star Trek      |

  Scenario: Retrieve all starships with an empty result set
    Given there are no starships in the system
    When I request all starships
    Then the system should return no results
    And I should receive an empty list