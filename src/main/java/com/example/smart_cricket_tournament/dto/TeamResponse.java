package com.example.smart_cricket_tournament.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record TeamResponse(
        Long id,
        String name,
        Long tournamentId,
        String tournamentName,
        List<PlayerResponse> players
) { }
