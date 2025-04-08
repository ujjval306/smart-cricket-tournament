package com.example.smart_cricket_tournament.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String role; // Batsman, Bowler, All-Rounder, Wicket-Keeper

    private int totalRuns;
    private int totalWickets;
    private int matchesPlayed;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    private boolean isCaptain;
    private boolean isViceCaptain;
    private boolean isSubstitute;
}
