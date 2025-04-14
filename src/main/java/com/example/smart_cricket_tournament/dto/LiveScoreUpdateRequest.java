package com.example.smart_cricket_tournament.dto;

import lombok.Data;

@Data
public class LiveScoreUpdateRequest {    private Integer teamARuns;
    private Integer teamAOvers;
    private Integer teamAWickets;

    private Integer teamBRuns;
    private Integer teamBOvers;
    private Integer teamBWickets;
}
