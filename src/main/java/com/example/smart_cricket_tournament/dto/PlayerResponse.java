package com.example.smart_cricket_tournament.dto;

import lombok.Builder;

@Builder
public record PlayerResponse(
        Long id,
        String name,
        String role,
        int totalRuns,
        int totalWickets,
        int matchesPlayed,
        boolean isCaptain,
        boolean isViceCaptain,
        boolean isSubstitute,
        Long teamId,
        String teamName
) { }
