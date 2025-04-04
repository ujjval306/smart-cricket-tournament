package com.example.smart_cricket_tournament.controller;

import com.example.smart_cricket_tournament.dto.AuthRequest;
import com.example.smart_cricket_tournament.dto.AuthResponse;
import com.example.smart_cricket_tournament.dto.RegisterRequest;
import com.example.smart_cricket_tournament.entity.User;
import com.example.smart_cricket_tournament.repository.UserRepository;
import com.example.smart_cricket_tournament.security.JwtService;
import com.example.smart_cricket_tournament.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@RequestBody @Validated RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            return ResponseEntity.badRequest().body(ApiResponse.<User>builder()
                    .success(false).message("Email already registered").build());
        }

        User user = User.builder()
                .fullName(request.fullName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role())
                .isAccountLocked(false)
                .isEnabled(true)
                .build();

       user = userRepository.save(user);


        return ResponseEntity.ok(new ApiResponse<>(true, "User registered", user));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody @Validated AuthRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtService.generateToken(user);

        AuthResponse authResponse = new AuthResponse(user,token);
        return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", authResponse));
    }
}