package com.woozy.carrentals.io.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.*;
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

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Email
    @Column(nullable = false, length = 120)
    private String email;

    @Column(nullable = false)
    @ToString.Exclude
    private String password;

    @Column(name = "email_verification_token")
    private String emailVerificationToken;

    @Builder.Default
    @Column(name = "email_verification_status", nullable = false)
    private Boolean emailVerificationStatus = false;

    @Pattern(regexp = "^\\+48[0-9]{9}$", message = "Invalid phone number")
    @Column(name = "mobile_number", nullable = false, length = 12)
    private String mobileNumber;

    @Column(nullable = false)
    @Min(21)
    @Max(65)
    private Integer age;

    @Column(name = "driving_license", nullable = false, length = 20)
    private String drivingLicense;

    @Column(nullable = false, length = 100)
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

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
