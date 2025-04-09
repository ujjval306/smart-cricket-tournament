package com.example.smart_cricket_tournament.controller;

import com.example.smart_cricket_tournament.dto.ScheduleMatchRequest;
import com.example.smart_cricket_tournament.dto.ScheduleMatchResponse;
import com.example.smart_cricket_tournament.entity.Match;
import com.example.smart_cricket_tournament.service.MatchService;
import com.example.smart_cricket_tournament.util.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/matches")
public class MatchController {
    private final MatchService matchService;

    @PostMapping("/schedule")
    public ResponseEntity<ApiResponse> scheduleMatch(@RequestBody @Validated ScheduleMatchRequest request){
        Match match = matchService.scheduleMatch(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Match scheduled successfully",match));
    }

    @GetMapping("/{tournamentId}")
    public ResponseEntity<ApiResponse<List<ScheduleMatchResponse>>> getMatchesByTournament(@PathVariable Long tournamentId) {
        List<ScheduleMatchResponse> matches = matchService.getMatchesByTournament(tournamentId);
        return ResponseEntity.ok(new ApiResponse<>(
                true, "Matches fetched", matches));
    }
}
