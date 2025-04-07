package com.example.smart_cricket_tournament.dto;

import java.time.LocalDate;

public record TournamentRequest(String name, String format, LocalDate startDate, LocalDate endDate) { }
