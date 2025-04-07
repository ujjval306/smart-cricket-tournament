package com.example.smart_cricket_tournament.controller;

import com.example.smart_cricket_tournament.dto.PlayerRequest;
import com.example.smart_cricket_tournament.dto.PlayerResponse;
import com.example.smart_cricket_tournament.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor

public class PlayerController {
    private final PlayerService playerService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEAM_MANAGER')")
    public PlayerResponse createPlayer(@RequestBody @Validated PlayerRequest request) {
        return playerService.createPlayer(request);
    }

    @GetMapping("/getAllPlayers")
    public List<PlayerResponse> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    @GetMapping("/getPlayerDetail/{id}")
    public PlayerResponse getPlayer(@PathVariable Long id) {
        return playerService.getPlayer(id);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEAM_MANAGER')")
    public PlayerResponse updatePlayer(@PathVariable Long id, @RequestBody @Validated PlayerRequest request) {
        return playerService.updatePlayer(id, request);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEAM_MANAGER')")
    public void deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
    }
}
