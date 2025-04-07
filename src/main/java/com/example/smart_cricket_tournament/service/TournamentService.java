package com.example.smart_cricket_tournament.service;

import com.example.smart_cricket_tournament.dto.TournamentRequest;
import com.example.smart_cricket_tournament.dto.TournamentResponse;
import com.example.smart_cricket_tournament.entity.Tournament;
import com.example.smart_cricket_tournament.repository.TournamentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TournamentService  {
    private final TournamentRepository tournamentRepository;

    public TournamentResponse createTournament(TournamentRequest request ){

        Tournament tournament = Tournament.builder()
                .name(request.name())
                .format(request.format())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .build();

        tournamentRepository.save(tournament);
        return mapToResponse(tournament);
    }

    public List<TournamentResponse> getAllTournaments(){
    return tournamentRepository.findAll().
            stream().map(this::mapToResponse).toList();
    }

    public TournamentResponse getTournament(Long id) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));
        return mapToResponse(tournament);
    }

    public TournamentResponse updateTournament(Long id, TournamentRequest request) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));

        tournament.setName(request.name());
        tournament.setFormat(request.format());
        tournament.setStartDate(request.startDate());
        tournament.setEndDate(request.endDate());

        return mapToResponse(tournamentRepository.save(tournament));
    }

    public void deleteTournament(Long id) {
        tournamentRepository.deleteById(id);
    }

    private TournamentResponse mapToResponse(Tournament t) {
        return new TournamentResponse(
                t.getId(),
                t.getName(),
                t.getFormat(),
                t.getStartDate(),
                t.getEndDate()
        );
    }
}
