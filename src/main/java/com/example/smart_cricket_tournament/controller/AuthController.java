package com.example.smart_cricket_tournament.controller;

import com.example.smart_cricket_tournament.dto.AuthRequest;
import com.example.smart_cricket_tournament.dto.AuthResponse;
import com.example.smart_cricket_tournament.dto.OtpVerificationRequest;
import com.example.smart_cricket_tournament.dto.RegisterRequest;
import com.example.smart_cricket_tournament.entity.User;
import com.example.smart_cricket_tournament.exception.BadRequestException;
import com.example.smart_cricket_tournament.repository.UserRepository;
import com.example.smart_cricket_tournament.security.JwtService;
import com.example.smart_cricket_tournament.service.EmailService;
import com.example.smart_cricket_tournament.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> sendOtpForRegistration(@RequestBody @Validated RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            return ResponseEntity.badRequest().body(ApiResponse.<User>builder()
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .message("Email already registered")
                    .build());
        }

        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);


        User user = User.builder()
                .fullName(request.fullName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role())
                .otp(otp)
                .otpExpiry(LocalDateTime.now().plusMinutes(5))
                .isVerified(false)
                .isAccountLocked(false)
                .isEnabled(true)
                .build();

        user = userRepository.save(user);

        emailService.sendVerificationEmail(user.getEmail(), otp);

        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "OTP sent to email", null));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<AuthResponse>> verifyOtp(@RequestBody @Validated OtpVerificationRequest request) {
        User user = userRepository.findByEmail(request.email()).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(HttpStatus.BAD_REQUEST, "User not found", null));
        }


        if (user.isVerified()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(HttpStatus.BAD_REQUEST, "User already verified", null));
        }

        if ( !request.otp().equals(user.getOtp())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(HttpStatus.UNAUTHORIZED, "Invalid OTP", null));
        }

        if( user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(HttpStatus.UNAUTHORIZED, "OTP expired", null));
        }

            user.setVerified(true);
            user.setOtp(null);
            user.setOtpExpiry(null);
            userRepository.save(user);


        String token = jwtService.generateToken(user);
        AuthResponse authResponse = new AuthResponse(user, token);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "OTP verified. Registration successful", authResponse));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody @Validated AuthRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (user.isAccountLocked()) {
            throw new BadRequestException("Your account is locked due to multiple failed login attempts.");
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {

            user.setFailedLoginAttempts((user.getFailedLoginAttempts() == null ? 0 : user.getFailedLoginAttempts()) + 1);
            if(user.getFailedLoginAttempts() >= 3) {
                user.setAccountLocked(true);
            }
            userRepository.save(user);
            throw new BadRequestException("Invalid credentials");
        }
        user.setFailedLoginAttempts(0);
        user.setAccountLocked(false);
        userRepository.save(user);

        String token = jwtService.generateToken(user);

        AuthResponse authResponse = new AuthResponse(user,token);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Login successful", authResponse));
    }
}