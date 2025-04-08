package com.example.smart_cricket_tournament.repository;

import com.example.smart_cricket_tournament.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
    boolean existsByNameAndTournamentId(String name, Long tournament_id);
}
