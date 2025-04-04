package com.example.smart_cricket_tournament.dto;

import com.example.smart_cricket_tournament.enums.Role;

public record RegisterRequest(
        String fullName,
        String email,
        String password,
        Role role
) {}