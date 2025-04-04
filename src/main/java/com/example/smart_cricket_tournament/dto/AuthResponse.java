package com.example.smart_cricket_tournament.dto;

import com.example.smart_cricket_tournament.entity.User;


public record AuthResponse(
        User user,
        String token
) {}
