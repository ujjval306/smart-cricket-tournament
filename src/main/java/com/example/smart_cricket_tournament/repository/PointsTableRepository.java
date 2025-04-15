package com.example.smart_cricket_tournament.repository;

import com.example.smart_cricket_tournament.dto.TeamResponse;
import com.example.smart_cricket_tournament.entity.PointsTable;
import com.example.smart_cricket_tournament.entity.Team;
import com.example.smart_cricket_tournament.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PointsTableRepository extends JpaRepository<PointsTable, Long> {
    Optional<PointsTable> findByTeamAndTournament(Team team, Tournament tournament);
    List<PointsTable> findAllByTournamentId(Long tournamentId);
}
