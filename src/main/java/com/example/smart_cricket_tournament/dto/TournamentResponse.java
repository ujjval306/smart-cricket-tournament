package com.example.smart_cricket_tournament.dto;

import java.time.LocalDate;

public record TournamentResponse(Long id, String name, String format, LocalDate startDate, LocalDate endDate) { }
