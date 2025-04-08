package com.example.smart_cricket_tournament.dto;

public record PlayerRequest(
        String name,
        String role, // Batsman, Bowler, etc.
        int totalRuns,
        int totalWickets,
        int matchesPlayed,
        boolean isCaptain,
        boolean isViceCaptain,
        boolean isSubstitute,
        Long teamId
) { }
