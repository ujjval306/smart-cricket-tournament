package com.example.smart_cricket_tournament.dto;



public record AuthRequest(
        String email,
        String password
) {}