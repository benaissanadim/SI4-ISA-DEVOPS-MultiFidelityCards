Feature: Store management

  Scenario: register a store in a specific zone
    When a merchant wants to add a store in a zone "new york"
    Then the number of stores should be 1
    And when a user consults stores in zone "new york"
    Then he should find the created store


    Scenario: add a store to favorites
      When I user adds the store to favorites
      Then the store should be in the list of favorites
