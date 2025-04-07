package com.example.smart_cricket_tournament.controller;

import com.example.smart_cricket_tournament.dto.TeamRequest;
import com.example.smart_cricket_tournament.dto.TeamResponse;
import com.example.smart_cricket_tournament.service.TeamService;
import com.example.smart_cricket_tournament.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor

public class TeamController {
    private final TeamService teamService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEAM_MANAGER')")
    public ResponseEntity<ApiResponse<TeamResponse>>  createTeam(@RequestBody @Validated TeamRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Team created", teamService.createTeam(request)));
    }

    @GetMapping("/getAllTeams")
    public List<TeamResponse> getAllTeams() {
        return teamService.getAllTeams();
    }

    @GetMapping("/getTeamDetail/{id}")
    public TeamResponse getTeam(@PathVariable Long id) {
        return teamService.getTeam(id);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEAM_MANAGER')")
    public ResponseEntity<ApiResponse<TeamResponse>>  updateTeam(@PathVariable Long id, @RequestBody @Validated TeamRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Team updated", teamService.updateTeam(id, request)));

    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEAM_MANAGER')")
    public ResponseEntity<ApiResponse<String>> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Tournament deleted", null));
    }
}
