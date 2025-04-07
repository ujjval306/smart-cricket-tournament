package com.example.smart_cricket_tournament.dto;

public record PlayerResponse(
        Long id,
        String name,
        String role,
        int totalRuns,
        int totalWickets,
        int matchesPlayed,
        boolean isCaptain,
        Long teamId,
        String teamName
) {
}
