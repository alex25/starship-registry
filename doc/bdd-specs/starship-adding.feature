Feature: Use case Add New Starship
  As a user of the Starship Registry system
  I want to add a new starship to the system
  So that I can register new starships and keep the database up-to-date

  Background:
    Given the Starship Registry application is running
    And the following movies exist in the system:
      | id  | title          |
      | 1   | Star Wars      |
      | 2   | Star Trek      |

  Scenario: Successfully add a new starship
    Given I want to add a new starship with the following details:
      | name           | movieTitle   |
      | Enterprise III | Star Wars    |
    When I submit the new starship details
    Then the starship should be added successfully
    And the starship should have an auto-generated ID
    And the cache for "starshipsByName", "starshipById", and "allStarships" should be cleared

  Scenario: Attempt to add a starship with a duplicate name
    Given I want to add a new starship with the following details:
      | name   | movieTitle   |
      | X-Wing | Star Wars    |
    And a starship with the name "X-Wing" already exists in the system
    When I attempt to add the new starship
    Then the system should prevent the addition
    And I should receive an error message stating "Starship with the same name already exists"

  Scenario: Handle database constraint violations when adding a starship
    Given I want to add a new starship with the following details:
      | name   | movieTitle   |
      | X-Wing | Star Wars    |
    And a database constraint violation occurs during the addition
    When I attempt to add the new starship
    Then the system should prevent the addition
    And I should receive an error message stating "Database constraint violation"

  Scenario: Clear cache after adding a new starship
    Given I want to add a new starship with the following details:
      | name           | movieTitle   |
      | Enterprise IV  | Rogue One    |
    When I submit the new starship details
    Then the starship should be added successfully
    And the cache for "starshipsByName", "starshipById", and "allStarships" should be cleared