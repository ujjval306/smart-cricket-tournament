package com.example.smart_cricket_tournament.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerMatchFormDTO {
    private Long matchId;
    private String matchDate;
    private String opponentTeam;
    private int runsScored;
    private int ballsFaced;
    private int wicketsTaken;
    private int oversBowled;
}
