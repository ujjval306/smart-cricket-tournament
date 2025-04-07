package com.example.smart_cricket_tournament.repository;

import com.example.smart_cricket_tournament.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> { }
