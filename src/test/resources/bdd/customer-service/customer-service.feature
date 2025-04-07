Feature: Customer service
  Customer should be able to update, delete and get its information through /customers endpoints

  @Customers @Cleanup
  Scenario: Client makes a call to GET /customers passing existing customer id
    When I call GET /customers passing existing customer userId
    Then I should receive response with status code of 200
    And body should contain customer details for provided customer userId

  @Customers @Cleanup
  Scenario: Client makes a call GET /customers passing non-existing customer id
    When I call GET /customers passing non-existing customer userId
    Then I should receive response with status code of 400
    And body sho uld contain error message "The user was not found"

  @Customers @Cleanup
  Scenario: Client makes a call DELETE /customers passing existing customer id
    When I call DELETE /customers passing existing userId
    Then I should receive response with status code of 204
    And customer should be removed from database

  @Customers @Cleanup
  Scenario: Client makes a call DELETE /customers passing non-existing customer id
    When I call DELETE /customers passing non-existing userId
    Then I should receive response with status code of 400
    And body should contain error message "The user was not found"

  @Customers @Cleanup
  Scenario: Client makes a call PUT /customers passing existing customer id and updated customer details
    Given the following customer details for update:
      | email                | mobileNumber | drivingLicense | address               |
      | john.doe@test.com    | +48501234567 | DL12345        | 123 Main St, Warsaw   |
      | alice.smith@test.com | +48602345678 | DL67890        | 456 Oak St, Kraków    |
      | bob.brown@test.com   | +48693456789 | DL54321        | 789 Pine St, Gdańsk   |
      | emma.j@test.com      | +48784567890 | DL98765        | 101 Cedar St, Wrocław |
    When I call PUT /customers passing existing userId and customer details for update
    Then I should receive response with status code of 200
    And body should contain updated customer details
    And customer details are updated in database

  @Customers @Cleanup
  Scenario: Client makes a call PUT /customers passing non-existing customer id and updated customer details
    Given the following customer details for update:
      | email              | mobileNumber | drivingLicense | address               |
      | john.doe@test.com  | +48501234567 | DL12345        | 123 Main St, Warsaw   |
      | ron.smith@test.com | +48602385678 | DL67899        | 456 Oak St, Kraków    |
      | bob.brown@test.com | +48697456789 | DL54327        | 789 Pine St, Gdańsk   |
      | gretta.j@test.com  | +48784567890 | DL98765        | 101 Cedar St, Wrocław |
    When I call PUT /customers passing non-existing userId and customer details for update
    Then I should receive response with status code of 400
    And body should contain error message "The user was not found"