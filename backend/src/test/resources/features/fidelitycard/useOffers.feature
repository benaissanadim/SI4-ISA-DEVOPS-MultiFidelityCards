Feature: create a fidelity card
  Background: a client
    Given client "nadim" with email "nadim@gmail.com" with satus "NORMAL" having fidelitycard number "12345678" with 50 points
    And store "polytech"

  Scenario: use parking NotEnoughPoints
    Given parking "parking1" in "nice" with points 70  "CLASSIC_OFFER"
    When "12345678" uses parking1
    Then exception "not enough points" is thrown

  Scenario: use parking NotELigible
    Given parking "parking1" in "nice" with points 50  "VFP_OFFER"
    When "12345678" uses parking1
    Then exception "not eligible for VFP offer" is thrown

  Scenario: use gift NotELigible
    Given client having no purchases in store "polytech"
    And gift "FREE_PIZZA" in "polytech" with points 30  "CLASSIC_OFFER"
    When "12345678" try to use gift
    Then exception "not eligible for gift" is thrown

  Scenario: use gift ELigible
    Given client having 2 purchases in store "polytech"
    And gift "FREE_PIZZA" in "polytech" with points 30  "CLASSIC_OFFER"
    When "12345678" try to use gift
    Then fidelity card has points 20
