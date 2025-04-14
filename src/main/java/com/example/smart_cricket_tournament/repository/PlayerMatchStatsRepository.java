package com.example.smart_cricket_tournament.repository;

import com.example.smart_cricket_tournament.entity.Player;
import com.example.smart_cricket_tournament.entity.PlayerMatchStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerMatchStatsRepository extends JpaRepository<PlayerMatchStats, Long> {
    List<PlayerMatchStats> findTop5ByPlayerOrderByMatch_MatchDateDesc(Player player);
}
