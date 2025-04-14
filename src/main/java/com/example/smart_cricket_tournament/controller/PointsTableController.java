package com.example.smart_cricket_tournament.controller;

import com.example.smart_cricket_tournament.entity.PointsTable;
import com.example.smart_cricket_tournament.service.PointsTableService;
import com.example.smart_cricket_tournament.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/points-table")
@RequiredArgsConstructor
public class PointsTableController {
    private final PointsTableService pointsTableService;

    @GetMapping("/{tournamentId}")
    public ResponseEntity<ApiResponse<List<PointsTable>>> getPointsTable(@PathVariable Long tournamentId) {
        List<PointsTable> pointsTable = pointsTableService.getPointsTableForTournament(tournamentId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Points table fetched successfully", pointsTable));
    }
}
