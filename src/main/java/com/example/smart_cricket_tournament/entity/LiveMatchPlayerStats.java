package com.example.smart_cricket_tournament.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class LiveMatchPlayerStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Match match;

    @ManyToOne
    private Player player;

    private boolean isStriker;
    private boolean isNonStriker;
    private boolean isBowler;

    private int runsScored;
    private int ballsFaced;

    private int wicketsTaken;
    private int oversBowled;
}
