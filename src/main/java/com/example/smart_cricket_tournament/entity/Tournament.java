package com.example.smart_cricket_tournament.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String format; // T20, ODI, Test

    private LocalDate startDate;
    private LocalDate endDate;

    @JsonManagedReference
    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private List<Team> teams = new ArrayList<>();
}
