package com.woozy.carrentals.io.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "mobile_number")
})
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", updatable = false, nullable = false, unique = true, columnDefinition = "CHAR(36)")
    private String userId;

    @NotEmpty(message = "First name must not be empty")
    @NotNull
    @Length(max = 50, message = "First name must be lower or equal to 50 characters")
    @Column(name = "first_name")
    private String firstName;

    @NotEmpty(message = "Last name must not be empty")
    @NotNull
    @Length(max = 50, message = "Last name must be lower or equal to 50 characters")
    @Column(name = "last_name")
    private String lastName;

    @Email(message = "Email must be well-formatted")
    @NotEmpty(message = "Email must not be empty")
    @NotNull
    @Length(max = 120, message = "Email must be lower or equal to 120 characters")
    @Column(nullable = false)
    private String email;

    @NotEmpty(message = "Password must not be empty")
    @Column(nullable = false)
    @ToString.Exclude
    private String password;

    @Column(name = "email_verification_token")
    private String emailVerificationToken;

    @Builder.Default
    @Column(name = "email_verification_status", nullable = false)
    private Boolean emailVerificationStatus = false;

    @NotEmpty(message = "Mobile number must not be empty")
    @NotNull
    @Pattern(regexp = "^\\+48[0-9]{9}$", message = "Invalid phone number")
    @Column(name = "mobile_number")
    private String mobileNumber;

    @NotNull
    @Min(21)
    @Max(65)
    private Integer age;

    @NotEmpty(message = "Driving license must not be empty")
    @NotNull
    @Length(max = 20, message = "Driving license must be lower or equal to 20 characters")
    @Column(name = "driving_license")
    private String drivingLicense;

    @NotEmpty(message = "Address must not be empty")
    @NotNull
    @Length(max = 100, message = "Address must be lower or equal to 100 characters")
    private String address;

    @UniqueElements()
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "roles", nullable = false)
    private Collection<Role> roles;

    @PrePersist
    public void prePersist() {
        if (userId == null) {
            userId = UUID.randomUUID().toString();
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .toList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
