package com.example.smart_cricket_tournament.controller;

import com.example.smart_cricket_tournament.dto.PlayerMatchFormDTO;
import com.example.smart_cricket_tournament.dto.PlayerRequest;
import com.example.smart_cricket_tournament.dto.PlayerResponse;
import com.example.smart_cricket_tournament.entity.Player;
import com.example.smart_cricket_tournament.repository.PlayerRepository;
import com.example.smart_cricket_tournament.service.PlayerService;
import com.example.smart_cricket_tournament.service.PlayerStatsService;
import com.example.smart_cricket_tournament.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor

public class PlayerController {
    private final PlayerService playerService;
    private final PlayerStatsService playerStatsService;
    private final PlayerRepository playerRepository;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEAM_MANAGER')")
    public ResponseEntity<ApiResponse<PlayerResponse>>  createPlayer(@RequestBody @Validated PlayerRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.CREATED,"Player created successfully",playerService.createPlayer(request)));
    }

    @GetMapping("/getAllPlayers")
    public ResponseEntity<ApiResponse<List<PlayerResponse>>>  getAllPlayers() {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK,"All Players Details",playerService.getAllPlayers()));
    }

    @GetMapping("/getPlayerDetail/{id}")
    public ResponseEntity<ApiResponse<PlayerResponse>> getPlayer(@PathVariable Long id) {
        return  ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Player detail fetched successfully",playerService.getPlayer(id))) ;
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEAM_MANAGER')")
    public ResponseEntity<ApiResponse<PlayerResponse>>  updatePlayer(@PathVariable Long id, @RequestBody @Validated PlayerRequest request) {
        PlayerResponse updated = playerService.updatePlayer(id, request);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.CREATED, "Player updated successfully", updated));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEAM_MANAGER')")
    public ResponseEntity<ApiResponse<String>> deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Player deleted", null));
    }

    @GetMapping("/{playerId}/last-5-matches")
    public ResponseEntity<ApiResponse<List<PlayerMatchFormDTO>>> getPlayerLast5Matches(@PathVariable Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        List<PlayerMatchFormDTO> form = playerStatsService.getLast5MatchesForm(player);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Last 5 matches fetched", form));
    }

}
