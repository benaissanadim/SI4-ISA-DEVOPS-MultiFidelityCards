Feature: Register clients
  Background: users in db
    Given given users "hamza" email "hamza@gmail.com" and "nadim" email "nadim@gmail.com"

  Scenario Outline: emails exceptions
    When user enters email <email> and username <username>
    Then exception is <msg>
    Examples:
      |   email           |   username            |     msg                  |
      | "nadim@gmail.com" |  "iI36Iql4fr#&8L"     | "client with email nadim@gmail.com already exists"    |
      | "hamza@gmail.com" |  "iI36Iql4fr#&8L"     | "client with email hamza@gmail.com already exists"    |
      | "hamza2@gmail.com" |  "hamza"             | "client with username hamza already exists"  |
      | "nadim2@gmail.com" |  "nadim"             | "client with username nadim already exists"   |

  Scenario Outline: emails passwords good
    When user enters email <email> and username <username>
    Then a new user is registered
    Examples:
      |   email           |   username            |
      | "nadio@gmail.com" |  "Nadimooo"       |
      | "ham@gmail.com"   |  "hamaa"     |
      | "SI4@gmail.com"   |    "nahuj"     |