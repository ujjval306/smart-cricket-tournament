package com.example.smart_cricket_tournament.service;

import com.example.smart_cricket_tournament.dto.PlayerRequest;
import com.example.smart_cricket_tournament.dto.PlayerResponse;
import com.example.smart_cricket_tournament.entity.Player;
import com.example.smart_cricket_tournament.entity.Team;
import com.example.smart_cricket_tournament.exception.BadRequestException;
import com.example.smart_cricket_tournament.exception.ResourceNotFoundException;
import com.example.smart_cricket_tournament.repository.PlayerRepository;
import com.example.smart_cricket_tournament.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    public PlayerResponse createPlayer(PlayerRequest request ){
        Team team = teamRepository.findById(request.teamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team not found"));

        // Captain check
        if(request.isCaptain()){
            playerRepository.findByTeamIdAndIsCaptainTrue(request.teamId())
                    .ifPresent(existingCaptain -> {
                        throw new BadRequestException("This team already has a captain");
                    });
        }

        // Check for duplicate player name
        boolean exists = playerRepository.existsByNameAndTeamId(request.name(), request.teamId());
        if (exists) {
            throw new BadRequestException("Player with this name already exists in the team");
        }

        // Total players check
        long totalPlayers = playerRepository.countByTeamId(team.getId());
        if (totalPlayers >= 15) {
            throw new BadRequestException("Team already has maximum of 15 players");
        }

        // Vice-Captain check
        if (request.isViceCaptain()) {
            boolean viceCaptainExists = playerRepository.existsByIsViceCaptainTrueAndTeamId(team.getId());
            if (viceCaptainExists) {
                throw new BadRequestException("Team already has a vice-captain");
            }
        }

        // Substitute count check
        if (request.isSubstitute()) {
            long substituteCount = playerRepository.countByIsSubstituteTrueAndTeamId(team.getId());
            if (substituteCount >= 4) {
                throw new BadRequestException("Team already has maximum of 4 substitute players");
            }
        } else {
            long mainPlayers = playerRepository.countByIsSubstituteFalseAndTeamId(team.getId());
            if (mainPlayers >= 11) {
                throw new BadRequestException("Team already has maximum of 11 main players");
            }
        }

        Player player = Player.builder()
                .name(request.name())
                .role(request.role())
                .totalRuns(request.totalRuns())
                .totalWickets(request.totalWickets())
                .matchesPlayed(request.matchesPlayed())
                .isCaptain(request.isCaptain())
                .isViceCaptain(request.isViceCaptain())
                .isSubstitute(request.isSubstitute())
                .team(team)
                .build();

        return mapToResponse(playerRepository.save(player));

    }

    public List<PlayerResponse> getAllPlayers() {
        return playerRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    public PlayerResponse getPlayer(Long id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Player not found"));
        return mapToResponse(player);
    }

    public PlayerResponse updatePlayer(Long id, PlayerRequest request) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found with ID " + id));

        Team team = teamRepository.findById(request.teamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID " + request.teamId()));

//        if (!player.getName().equalsIgnoreCase(request.name()) || !player.getTeam().getId().equals(request.teamId())) {
//            boolean exists = playerRepository.existsByNameAndTeamId(request.name(), request.teamId());
//            if (exists) {
//                throw new BadRequestException("Another player with this name already exists in the team");
//            }
//        }

        player.setName(request.name());
        player.setRole(request.role());
        player.setTotalRuns(request.totalRuns());
        player.setTotalWickets(request.totalWickets());
        player.setMatchesPlayed(request.matchesPlayed());
        player.setCaptain(request.isCaptain());
        player.setViceCaptain(request.isViceCaptain());
        player.setSubstitute(request.isSubstitute());
        player.setTeam(team);

        return mapToResponse(playerRepository.save(player));
    }

    public void deletePlayer(Long id) {
        playerRepository.deleteById(id);
    }

    private PlayerResponse mapToResponse(Player player) {
        return new PlayerResponse(
                player.getId(),
                player.getName(),
                player.getRole(),
                player.getTotalRuns(),
                player.getTotalWickets(),
                player.getMatchesPlayed(),
                player.isCaptain(),
                player.isViceCaptain(),
                player.isSubstitute(),
                player.getTeam().getId(),
                player.getTeam().getName()
        );
    }
}
