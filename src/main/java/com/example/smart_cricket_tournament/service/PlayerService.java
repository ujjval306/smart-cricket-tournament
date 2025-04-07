package com.example.smart_cricket_tournament.service;

import com.example.smart_cricket_tournament.dto.PlayerRequest;
import com.example.smart_cricket_tournament.dto.PlayerResponse;
import com.example.smart_cricket_tournament.entity.Player;
import com.example.smart_cricket_tournament.entity.Team;
import com.example.smart_cricket_tournament.exception.BadRequestException;
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
                .orElseThrow(() -> new RuntimeException("Team not found"));

        if(request.isCaptain()){
            playerRepository.findByTeamIdAndIsCaptainTrue(request.teamId())
                    .ifPresent(existingCaptain -> {
                        throw new RuntimeException("This team already has a captain: " + existingCaptain.getName());
                    });
        }

        boolean exists = playerRepository.existsByNameAndTeamId(request.name(), request.teamId());
        if (exists) {
            throw new BadRequestException("Player with this name already exists in the team",HttpStatus.BAD_REQUEST);
        }

        Player player = Player.builder()
                .name(request.name())
                .role(request.role())
                .totalRuns(request.totalRuns())
                .totalWickets(request.totalWickets())
                .matchesPlayed(request.matchesPlayed())
                .isCaptain(request.isCaptain())
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
                .orElseThrow(() -> new RuntimeException("Player not found"));

        Team team = teamRepository.findById(request.teamId())
                .orElseThrow(() -> new RuntimeException("Team not found"));

        if (!player.getName().equalsIgnoreCase(request.name()) || !player.getTeam().getId().equals(request.teamId())) {
            boolean exists = playerRepository.existsByNameAndTeamId(request.name(), request.teamId());
            if (exists) {
                throw new BadRequestException("Another player with this name already exists in the team", HttpStatus.BAD_REQUEST);
            }
        }

        player.setName(request.name());
        player.setRole(request.role());
        player.setTotalRuns(request.totalRuns());
        player.setTotalWickets(request.totalWickets());
        player.setMatchesPlayed(request.matchesPlayed());
        player.setCaptain(request.isCaptain());
        player.setTeam(team);

        return mapToResponse(playerRepository.save(player));
    }

    public void deletePlayer(Long id) {
        playerRepository.deleteById(id);
    }

    private PlayerResponse mapToResponse(Player p) {
        return new PlayerResponse(
                p.getId(),
                p.getName(),
                p.getRole(),
                p.getTotalRuns(),
                p.getTotalWickets(),
                p.getMatchesPlayed(),
                p.isCaptain(),
                p.getTeam().getId(),
                p.getTeam().getName()
        );
    }
}
