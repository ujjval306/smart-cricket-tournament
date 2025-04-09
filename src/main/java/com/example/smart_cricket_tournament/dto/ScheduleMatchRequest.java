package com.example.smart_cricket_tournament.dto;

import com.example.smart_cricket_tournament.enums.MatchFormat;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ScheduleMatchRequest {
    private Long tournamentId;

    private Long teamAId;

    private Long teamBId;

    private LocalDate matchDate;
    private LocalTime matchTime;

    private String venue;

    private MatchFormat format;
}
