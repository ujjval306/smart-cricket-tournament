package com.example.smart_cricket_tournament.dto;

import lombok.Data;

@Data
public class MatchResultRequest {
    private Long winnerTeamId;
    private Integer teamARuns;
    private Integer teamAOvers;
    private Integer teamBRuns;
    private Integer teamBOvers;
}
