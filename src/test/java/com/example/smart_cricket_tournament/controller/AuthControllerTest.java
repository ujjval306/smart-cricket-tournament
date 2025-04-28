package com.example.smart_cricket_tournament.controller;

import com.example.smart_cricket_tournament.dto.AuthRequest;
import com.example.smart_cricket_tournament.dto.OtpVerificationRequest;
import com.example.smart_cricket_tournament.dto.RegisterRequest;
import com.example.smart_cricket_tournament.entity.User;
import com.example.smart_cricket_tournament.enums.Role;
import com.example.smart_cricket_tournament.repository.UserRepository;
import com.example.smart_cricket_tournament.security.JwtService;
import com.example.smart_cricket_tournament.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(AuthControllerTestConfig.class)

public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(userRepository, passwordEncoder, jwtService, emailService)).build();
    }

    @Test
    @WithMockUser
    void register_whenEmailAlreadyExists_thenReturnBadRequest() throws Exception {
        RegisterRequest request = new RegisterRequest("John Doe", "admin@yopmail.com", "Admin@123", Role.ADMIN);

        when(userRepository.findByEmail("admin@yopmail.com")).thenReturn(Optional.of(new User()));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Email already registered"));
    }

    @Test
    @WithMockUser
    void register_whenNewEmail_thenSendOtpAndReturnOk() throws Exception {
        String email = "admin@yopmail.com";
        String password = "Admin@123";
        RegisterRequest request = new RegisterRequest("John Doe", email, password, Role.ADMIN);

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OTP sent to email"));
    }

    @Test
     void verifyOtp_whenUserNotFound_thenReturnBadRequest() throws Exception {
        String email = "abc@yopmail.com";
        OtpVerificationRequest request = new OtpVerificationRequest(email, "123456");

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/auth/verify-otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User not found"));
    }


    @Test
    public void testLogin_userNotFound() throws Exception {
        // Arrange
        AuthRequest request = new AuthRequest("admin@yopmail.com", "Admin@123");
        when(userRepository.findByEmail("admin@yopmail.com")).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    public void testLogin_invalidCredentials() throws Exception {
        // Arrange
        String email = "admin@yopmail.com";
        User user = new User();
        user.setPassword("encodedPassword");
        AuthRequest request = new AuthRequest(email, "wrongPassword");
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid credentials"));
    }

    @Test
    public void testLogin_success() throws Exception {
        // Arrange
        String email = "admin@yopmail.com";
        User user = new User();
        user.setPassword("encodedPassword");
        AuthRequest request = new AuthRequest(email, "Admin@123");
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("token123");

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.data.token").value("token123"));
    }

}
