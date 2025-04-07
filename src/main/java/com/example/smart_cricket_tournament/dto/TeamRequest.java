package com.example.smart_cricket_tournament.dto;

public record TeamRequest(
        String name,
        String city,
        Long tournamentId
) { }
