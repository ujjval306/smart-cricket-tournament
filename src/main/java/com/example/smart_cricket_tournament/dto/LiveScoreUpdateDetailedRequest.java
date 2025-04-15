package com.example.smart_cricket_tournament.dto;


public record LiveScoreUpdateDetailedRequest(
        Integer teamARuns,
        Integer teamAOvers,
        Integer teamAWickets,
        Integer teamBRuns,
        Integer teamBOvers,
        Integer teamBWickets,

        Long strikerId,
        Long nonStrikerId,
        Long bowlerId,

        Integer strikerRuns,
        Integer strikerBalls,
        Integer nonStrikerRuns,
        Integer nonStrikerBalls,

        Integer bowlerWickets,
        Integer bowlerOvers
) {
}
