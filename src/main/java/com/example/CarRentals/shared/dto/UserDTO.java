package com.example.CarRentals.shared.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class UserDTO implements Serializable {
    @Serial
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String encryptedPassword;
    private Long mobileNumber;
    private Integer age;
    private String drivingLicense;
    private String address;
    private String emailVerificationToken;
    private Boolean emailVerificationStatus = false;
}
