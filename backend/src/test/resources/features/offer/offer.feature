Feature: Create a gift

  Scenario: add a new gift
    When a merchant adds a new gift in new zone "antibes"
    Then the number of gifts should increase by 1
    And when a user explore offers in this zone
    Then he should see the new gift


    Scenario: create a parking offer
    When a merchant adds a new parking offer in new zone "Nice"
    Then the number of parking offers should increase by 1
    And when a user explore offers in "Nice"
    Then he should see the new parking offer
