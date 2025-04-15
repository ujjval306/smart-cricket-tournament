package com.example.smart_cricket_tournament.controller;

import com.example.smart_cricket_tournament.dto.*;
import com.example.smart_cricket_tournament.entity.Match;
import com.example.smart_cricket_tournament.service.MatchService;
import com.example.smart_cricket_tournament.util.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ApiResponse<ScheduleMatchResponse>> scheduleMatch(@RequestBody @Validated ScheduleMatchRequest request){
        ScheduleMatchResponse response = matchService.scheduleMatch(request);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.CREATED, "Match scheduled successfully",response));
    }

    @GetMapping("/{tournamentId}")
    public ResponseEntity<ApiResponse<List<ScheduleMatchResponse>>> getMatchesByTournament(@PathVariable Long tournamentId) {
        List<ScheduleMatchResponse> matches = matchService.getMatchesByTournament(tournamentId);
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK, "Matches fetched", matches));
    }

    @PutMapping("/{matchId}/result")
    public ResponseEntity<ApiResponse<ScheduleMatchResponse>> updateMatchResult(
            @PathVariable Long matchId,
            @RequestBody MatchResultRequest request) {
        Match updatedMatch = matchService.updateMatchResult(matchId, request);
        ScheduleMatchResponse response = matchService.mapToResponse(updatedMatch);

        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Match result updated and points table updated", response));
    }

    @PutMapping("/{matchId}/live-score")
    public ResponseEntity<ApiResponse<ScheduleMatchResponse>> updateLiveScore(
            @PathVariable Long matchId,
            @RequestBody LiveScoreUpdateDetailedRequest request) {

        Match updated = matchService.updateLiveScore(matchId, request);
        ScheduleMatchResponse response = matchService.mapToResponse(updated);

        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Live score updated", response));
    }

}
