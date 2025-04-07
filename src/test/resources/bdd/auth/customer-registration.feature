Feature: Customer registration
  Customer should be able to register through authentication API endpoints


  @Reg @Cleanup
  Scenario: Client makes a call to POST /register-customer endpoint with valid customer details
    Given the following customer valid details:
      | firstName | lastName | email                | password  | mobileNumber | age | drivingLicense | address               |
      | John      | Doe      | john.doe@test.com    | Pass@123  | +48501234567 | 30  | DL12345        | 123 Main St, Warsaw   |
      | Alice     | Smith    | alice.smith@test.com | P@ssw0rd  | +48602345678 | 25  | DL67890        | 456 Oak St, Kraków    |
      | Bob       | Brown    | bob.brown@test.com   | Test@321  | +48693456789 | 40  | DL54321        | 789 Pine St, Gdańsk   |
      | Emma      | Johnson  | emma.j@test.com      | MyP@ss123 | +48784567890 | 35  | DL98765        | 101 Cedar St, Wrocław |

    When client calls POST /register-customer with customer details
    Then I should receive response with status code of 200
    And body contains saved customer details with non-null values

  @Reg
  Scenario: Client makes a call to POST /register-customer endpoint with invalid customer details
    Given the following customer invalid details:
      | firstName | lastName | email                  | password  | mobileNumber | age | drivingLicense | address               | errorMessage                       |
      | John      | Doe      | john.doe@test.com      | Pass@123  | +48123456789 | 19  | DL12345        | 123 Main St, Warsaw   | # Age below minimum (21)           |
      | Alice     | Smith    | alice.smith.com        | P@ssw0rd  | +48602345678 | 25  | DL67890        | 456 Oak St, Kraków    | # Invalid email format (missing @) |
      | Bob       | Brown    | bob.brown@test.com     | Test@321  | 123456789    | 40  | DL54321        | 789 Pine St, Gdańsk   | # Invalid phone number             |
      | Emma      | Johnson  | emma.j@test.com        | MyP@ss123 | +48784567890 | 70  | DL98765        | 101 Cedar St, Wrocław | # Age above maximum (65)           |
      | Tom       | [blank]  | tom.white@test.com     | Pass@999  | +48555666777 | 30  | DL77777        | 222 Elm St, Poznań    | # Blank last name                  |
      | Sarah     | Green    |                        | S@rah123  | +48999888777 | 28  | DL99999        | 333 Maple St, Łódź    | # Missing email                    |
      | Kevin     | Black    | kevin.black@test.com   | [blank]   | +48777555444 | 45  | DL55555        | 555 Birch St, Gdynia  | # Blank password                   |
      | Olivia    | Adams    | olivia.adams@test.com  | Passw0rd! | +48888444222 | 35  | [blank]        | 666 Willow St, Lublin | # Blank driving license            |
      | Daniel    | Carter   | daniel.carter@test.com | Passw0rd1 | +48712345678 | 32  | DL12121        | [blank]               | # Blank address                    |

    When client calls POST /register-customer with customer details
    Then I should receive response with status code of 400
    And body contains the following error message:
      | Age must be greater than or equal to 21 |
      | Email must be well-formatted            |
      | Invalid phone number                    |
      | Age must be less than or equal to 65    |
      | Last name must not be empty             |
      | Email can not be null                   |
      | Password can not be null                |
      | Driving license must not be empty       |
      | Address must not be empty               |


  @Reg @Cleanup
  Scenario: ClienAutht makes a call to POST /register-customer endpoint with existing email
    Given customer is read from register_customer_dto.csv file
    And customer is saved to database
    When client calls POST /register-customer with the same email as existing one in database
    Then I should receive response with status code of 400
    And error message in body contains "The customer email or phone number already exists"

