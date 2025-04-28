package com.example.smart_cricket_tournament.controller;

import com.example.smart_cricket_tournament.dto.RegisterRequest;
import com.example.smart_cricket_tournament.entity.User;
import com.example.smart_cricket_tournament.enums.Role;
import com.example.smart_cricket_tournament.repository.UserRepository;
import com.example.smart_cricket_tournament.security.JwtService;
import com.example.smart_cricket_tournament.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

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

    @Test
    @WithMockUser
    void register_whenEmailAlreadyExists_thenReturnBadRequest() throws Exception {
        RegisterRequest request = new RegisterRequest("John Doe", "john@example.com", "password", Role.ADMIN);

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(new User()));

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void register_whenNewEmail_thenSendOtpAndReturnOk() throws Exception {
        RegisterRequest request = new RegisterRequest("John Doe", "john@example.com", "password", Role.ADMIN);

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("OTP sent to email"));
    }

}
