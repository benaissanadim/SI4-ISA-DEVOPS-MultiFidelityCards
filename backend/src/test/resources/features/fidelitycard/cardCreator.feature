Feature: create a fidelity card
  Background: a client
    Given given client "nadim" email "nadim@gmail.com"
    And zone "antibes"

  Scenario: create fidelityCard success
    When client "nadim" creates new card in "antibes"
    Then card created successfully
    And client can't create another card

  Scenario Outline: create fidelityCard unsuccessfully
    When client <username> creates new card in <city>
    Then exception <msg>
    Examples:
      | username | city     | msg                                         |
      | "nadim"  | "nice"   |"Geographic zone nice does not exists"   |
      | "nadim"  | "paris"   |"Geographic zone paris does not exists"   |
      | "nadim"  | "tunis"   |"Geographic zone tunis does not exists"   |
