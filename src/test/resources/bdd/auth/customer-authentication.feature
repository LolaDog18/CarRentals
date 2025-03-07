Feature: Customer authentication
  Customer should be able to authenticate through authentication API endpoint

  @Auth @Cleanup
  Scenario Template: Registered customer makes a call to /authenticate endpoint with valid credentials
    Given I register a customer through POST /register-customer with following customer details:
      | firstName | lastName | email                  | password | mobileNumber | age | drivingLicense | address             |
      | Jami      | Lanister | jami.lanister@test.com | Pass@128 | +48501234567 | 30  | DL12345        | 123 Main St, Warsaw |
    When I call POST /authenticate endpoint with <email> and <password>
    Then I should receive response with status code of 200
    And body should contain authentication token

    Examples:
      | email                  | password |
      | jami.lanister@test.com | Pass@128 |

  @Auth
  Scenario Template: Non-registered customer makes a call to /authenticate endpoint
    Given I don't register a customer
    When I call POST /authenticate endpoint with <email> and <password>
    Then I should receive response with status code of 400
    And body should contain error message "The user was not found"

    Examples:
      | email              | password |
      | jhon.snow@test.com | Pass@123 |

  @Auth @Cleanup
  Scenario Template: Registered customer makes a call to /authenticate endpoint with invalid password
    Given I register a customer through POST /register-customer with following customer details:
      | firstName | lastName | email                | password | mobileNumber | age | drivingLicense | address             |
      | Sansa     | Stark    | sansa.stark@test.com | Pas@158  | +48501234567 | 30  | DL12345        | 123 Main St, Warsaw |
    When I call POST /authenticate endpoint with <email> and <password>
    Then I should receive response with status code of 401
    And body should contain error message "The user is not authorized"

    Examples:
      | email                | password |
      | sansa.stark@test.com | Pass@123 |
