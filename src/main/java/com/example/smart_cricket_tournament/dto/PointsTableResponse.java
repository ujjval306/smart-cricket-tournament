package com.example.smart_cricket_tournament.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PointsTableResponse {
    private String teamName;
    private int matchesPlayed;
    private int wins;
    private int losses;
    private int points;
    private double netRunRate;
}
