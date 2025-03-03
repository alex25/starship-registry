Feature: Use case Remove Starship
  As a user of the Starship Registry system
  I want to remove an existing starship
  So that I can delete outdated or unnecessary starship information

  Background:
    Given the Starship Registry application is running
    And the following starships exist in the system:
      | id  | name               | movieTitle     |
      | 1   | Millennium Falcon  | Star Wars      |
      | 2   | USS Enterprise     | Star Trek      |

  Scenario: Successfully remove an existing starship
    Given I want to remove the starship with ID "1"
    When I delete the starship
    Then the starship should be removed successfully
    And the starship with ID "1" should no longer exist in the system

  Scenario: Attempt to remove a non-existent starship
    Given I want to remove the starship with ID "999"
    When I attempt to delete the starship
    Then the system should prevent the removal
    And I should receive an error message stating "Starship with ID 999 not found"

  Scenario: Attempt to remove a starship with existing dependencies
    Given the starship with ID "2" has dependencies in the system
    When I attempt to delete the starship with ID "2"
    Then the system should prevent the removal
    And I should receive an error message stating "Starship with ID 2 cannot be deleted due to existing dependencies"

  Scenario: Clear cache after removing a starship
    Given I want to remove the starship with ID "1"
    When I delete the starship
    Then the starship should be removed successfully
    And the cache for "starshipsByName", "starshipById", and "allStarships" should be cleared