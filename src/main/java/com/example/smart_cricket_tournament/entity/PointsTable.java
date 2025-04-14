package com.example.smart_cricket_tournament.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "points_table")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PointsTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Team team;

    @ManyToOne
    private Tournament tournament;

    private int matchesPlayed;
    private int wins;
    private int losses;
    private int ties;
    private int noResults;
    private int points;

    private int runsScored;
    private double oversFaced;

    private int runsConceded;
    private double oversBowled;

    private double netRunRate;
}
