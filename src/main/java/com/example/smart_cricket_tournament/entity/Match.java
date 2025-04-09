package com.example.smart_cricket_tournament.entity;

import com.example.smart_cricket_tournament.enums.MatchFormat;
import com.example.smart_cricket_tournament.enums.MatchStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "`match`")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Tournament tournament;

    @ManyToOne
    private Team teamA;

    @ManyToOne
    private Team teamB;

    private LocalDate matchDate;
    private LocalTime matchTime;
    private String venue;

    @Enumerated(EnumType.STRING)
    private MatchFormat format;

    @Enumerated(EnumType.STRING)
    private MatchStatus status = MatchStatus.SCHEDULED; // SCHEDULED, COMPLETED, etc.

    private String result;
}
