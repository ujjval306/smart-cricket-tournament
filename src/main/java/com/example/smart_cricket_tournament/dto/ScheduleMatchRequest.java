package com.example.smart_cricket_tournament.dto;

import com.example.smart_cricket_tournament.enums.MatchFormat;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ScheduleMatchRequest {
    @NotNull
    private Long tournamentId;

    @NotNull
    private Long teamAId;

    @NotNull
    private Long teamBId;

    @NotNull
    private LocalDate matchDate;

    @NotNull
    private LocalTime matchTime;

    private String venue;

    @NotNull
    private MatchFormat format;
}
