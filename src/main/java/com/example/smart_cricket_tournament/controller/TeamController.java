package com.example.smart_cricket_tournament.controller;

import com.example.smart_cricket_tournament.dto.TeamRequest;
import com.example.smart_cricket_tournament.dto.TeamResponse;
import com.example.smart_cricket_tournament.service.TeamService;
import com.example.smart_cricket_tournament.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.CREATED, "Team created", teamService.createTeam(request)));
    }

    @GetMapping("/getAllTeams")
    public ResponseEntity<ApiResponse<List<TeamResponse>>> getAllTeams() {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK,"All teams fetched successfully",teamService.getAllTeams()));
    }

    @GetMapping("/getTeamDetail/{id}")
    public ResponseEntity<ApiResponse<TeamResponse>>  getTeam(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK,"Team detail fetched successfully",teamService.getTeam(id)));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEAM_MANAGER')")
    public ResponseEntity<ApiResponse<TeamResponse>>  updateTeam(@PathVariable Long id, @RequestBody @Validated TeamRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.CREATED, "Team updated", teamService.updateTeam(id, request)));

    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEAM_MANAGER')")
    public ResponseEntity<ApiResponse<String>> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Tournament deleted", null));
    }
}
