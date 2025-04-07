package com.example.smart_cricket_tournament.dto;

public record TeamResponse(
        Long id,
        String name,
        String city,
        Long tournamentId,
        String tournamentName
) { }
