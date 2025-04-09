package com.example.smart_cricket_tournament.repository;

import com.example.smart_cricket_tournament.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    boolean existsByTournamentIdAndTeamAIdAndTeamBIdAndMatchDate(Long tournamentId, Long teamAId, Long teamBId, LocalDate matchDate);
    List<Match> findByTournamentId(Long tournamentId);
}
