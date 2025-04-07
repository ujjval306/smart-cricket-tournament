package com.example.smart_cricket_tournament.controller;

import com.example.smart_cricket_tournament.dto.TournamentRequest;
import com.example.smart_cricket_tournament.dto.TournamentResponse;
import com.example.smart_cricket_tournament.service.TournamentService;
import com.example.smart_cricket_tournament.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
public class TournamentController {
    private final TournamentService tournamentService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<TournamentResponse>> create(@RequestBody TournamentRequest request){
        return ResponseEntity.ok(new ApiResponse<>(true,"Tournament created",tournamentService.createTournament(request)));
    }

    @GetMapping("/getAllTournaments")
    public ResponseEntity<ApiResponse<List<TournamentResponse>>> getAll() {
        return ResponseEntity.ok(new ApiResponse<>(true, "List of tournaments", tournamentService.getAllTournaments()));
    }

    @GetMapping("/getTournament/{id}")
    public ResponseEntity<ApiResponse<TournamentResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Tournament details", tournamentService.getTournament(id)));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<TournamentResponse>> update(@PathVariable Long id, @RequestBody TournamentRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Tournament updated", tournamentService.updateTournament(id, request)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        tournamentService.deleteTournament(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Tournament deleted", null));
    }
}
