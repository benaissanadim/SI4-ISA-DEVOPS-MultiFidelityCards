Feature: Register a new geographic zone

  Scenario : add new zone
    When admin add new zone "antibes"
    Then number of zones is now 1
    And When admin try to add again the same zone "antibes"
    Then exception "Geographic zone antibes already exists"
