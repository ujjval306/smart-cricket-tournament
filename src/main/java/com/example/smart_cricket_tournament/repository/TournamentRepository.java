package com.example.smart_cricket_tournament.repository;

import com.example.smart_cricket_tournament.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentRepository extends JpaRepository<Tournament, Long> { }
