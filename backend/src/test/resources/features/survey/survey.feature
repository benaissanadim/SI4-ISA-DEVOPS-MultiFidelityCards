Feature: Survey management
  Background: a client
    And survey "Stores" with 2 question "Do u like the store ?" "what other types of store u like ?"

  Scenario: add a new survey
    When admin adds survey "PARKING" with questions "What do u like about new service parking ?" , "do u suggest other offers ?"
    Then number of survies equal to 2
    And number of questions equal to 4

  Scenario: respond to survey
    Given client "philippe" and e-mail "philipe@gmail.com"
    When client respond to question 1 "Fantastc !" of survey "Stores"
    Then question 1 for survey has 1 answer "Fantastc !" from "philippe"

  Scenario: delete survey
    When admin delete survey "Stores"
    Then number of survies equal to 0