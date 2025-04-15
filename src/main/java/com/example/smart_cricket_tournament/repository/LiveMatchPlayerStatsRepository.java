package com.example.smart_cricket_tournament.repository;

import com.example.smart_cricket_tournament.entity.LiveMatchPlayerStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LiveMatchPlayerStatsRepository extends JpaRepository<LiveMatchPlayerStats, Integer> {
    Optional<LiveMatchPlayerStats> findByMatchIdAndPlayerId(Long matchId, Long playerId);
}
