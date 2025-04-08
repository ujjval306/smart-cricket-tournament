package com.example.smart_cricket_tournament.repository;

import com.example.smart_cricket_tournament.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface MatchRepository extends JpaRepository<Match, Long> {
    boolean existsByTournamentIdAndTeamAIdAndTeamBIdAndMatchDate(Long tournamentId, Long teamAId, Long teamBId, LocalDate matchDate);
}
