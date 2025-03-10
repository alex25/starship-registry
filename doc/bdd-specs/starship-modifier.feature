Feature: Use case Modify Starship
  As a user of the Starship Registry system
  I want to modify the details of an existing starship
  So that I can keep the starship information up-to-date

  Background:
    Given the Starship Registry application is running
    And the following starships exist in the system:
      | id  | name          | movieTitle     |
      | 1   | Millennium Falcon | Star Wars      |
      | 2   | USS Enterprise   | Star Trek      |

  Scenario: Successfully modify an existing starship
    Given I want to modify the starship with ID "1"
    When I update the starship details with the following data:
      | name                | movieTitle     |
      | Millennium Falcon 2 | The Last Jedi  |
    Then the starship details should be updated successfully
    And the starship with ID "1" should have the following details:
      | name                | movieTitle     |
      | Millennium Falcon 2 | The Last Jedi  |

  Scenario: Attempt to modify a starship with a duplicate name
    Given I want to modify the starship with ID "2"
    When I update the starship details with the following data:
      | name                | movieTitle     |
      | Millennium Falcon   | Star Wars      |
    Then the system should prevent the modification
    And I should receive an error message stating "Starship with the same name already exists"

  Scenario: Attempt to modify a non-existent starship
    Given I want to modify the starship with ID "999"
    When I attempt to update the starship details
    Then the system should prevent the modification
    And I should receive an error message stating "Starship with ID 999 not found"

  Scenario: Clear cache after modifying a starship
    Given I want to modify the starship with ID "1"
    When I update the starship details with the following data:
      | name                | movieTitle     |
      | Millennium Falcon 3 | Rogue One      |
    Then the starship details should be updated successfully
    And the cache for "starshipsByName", "starshipById", and "allStarships" should be cleared