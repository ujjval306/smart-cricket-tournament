package com.example.smart_cricket_tournament.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder

public record ScheduleMatchResponse(
        Long matchId,
        Long tournamentId,
        String tournamentName,

        Long teamAId,
        String teamAName,

        Long teamBId,
        String teamBName,

        LocalDate matchDate,
        LocalTime matchTime,
        String venue
) {

}
