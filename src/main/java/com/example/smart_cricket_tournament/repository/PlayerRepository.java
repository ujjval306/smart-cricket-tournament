package com.example.smart_cricket_tournament.repository;

import com.example.smart_cricket_tournament.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByTeamIdAndIsCaptainTrue(Long teamId);
    boolean existsByNameAndTeamId(String name, Long teamId);

    boolean existsByIsViceCaptainTrueAndTeamId(Long teamId);

    long countByIsSubstituteTrueAndTeamId(Long teamId);

    long countByIsSubstituteFalseAndTeamId(Long teamId);

    long countByTeamId(Long teamId);

}
