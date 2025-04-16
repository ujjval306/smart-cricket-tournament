package com.example.smart_cricket_tournament.entity;

import com.example.smart_cricket_tournament.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "failed_login_attempts" , nullable = false)
    private Integer failedLoginAttempts;

    @Column(name = "account_locked", nullable = false)
    private boolean isAccountLocked = false;

    private boolean isEnabled;

    @Column(name = "otp")
    private String otp;

    @Column(name = "otp_expiry")
    private LocalDateTime otpExpiry;

    @Column(name = "is_verified")
    private boolean isVerified = false;
}
