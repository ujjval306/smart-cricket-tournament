package com.example.smart_cricket_tournament.service;

import com.example.smart_cricket_tournament.dto.ScheduleMatchRequest;
import com.example.smart_cricket_tournament.dto.ScheduleMatchResponse;
import com.example.smart_cricket_tournament.entity.Match;
import com.example.smart_cricket_tournament.entity.Team;
import com.example.smart_cricket_tournament.entity.Tournament;
import com.example.smart_cricket_tournament.enums.MatchStatus;
import com.example.smart_cricket_tournament.exception.BadRequestException;
import com.example.smart_cricket_tournament.exception.ResourceNotFoundException;
import com.example.smart_cricket_tournament.repository.MatchRepository;
import com.example.smart_cricket_tournament.repository.TeamRepository;
import com.example.smart_cricket_tournament.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MatchService {
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;

    public Match scheduleMatch(ScheduleMatchRequest request) {

        Tournament tournament = tournamentRepository.findById(request.getTournamentId())
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found"));

        Team teamA = teamRepository.findById(request.getTeamAId())
                .orElseThrow(() -> new ResourceNotFoundException("Team A not found"));

        Team teamB = teamRepository.findById(request.getTeamBId())
                .orElseThrow(() -> new ResourceNotFoundException("Team B not found"));

        if (teamA.getId().equals(teamB.getId())) {
            throw new BadRequestException("A team cannot play against itself");
        }

        if (!tournament.getTeams().contains(teamA) || !tournament.getTeams().contains(teamB)) {
            throw new BadRequestException("Both teams must be part of the selected tournament");
        }

        if (matchRepository.existsByTournamentIdAndTeamAIdAndTeamBIdAndMatchDate(
                tournament.getId(), teamA.getId(), teamB.getId(), request.getMatchDate())) {
            throw new BadRequestException("Match already scheduled between these teams on this date");
        }

        Match match = new Match();
        match.setTournament(tournament);
        match.setTeamA(teamA);
        match.setTeamB(teamB);
        match.setMatchDate(request.getMatchDate());
        match.setMatchTime(request.getMatchTime());
        match.setVenue(request.getVenue());
        match.setFormat(request.getFormat());
        match.setStatus(MatchStatus.SCHEDULED);

        return matchRepository.save(match);
    }

    public List<ScheduleMatchResponse> getMatchesByTournament(Long tournamentId) {
        List<Match> matches = matchRepository.findByTournamentId(tournamentId);
        return matches.stream().map(this::mapToResponse).toList();
    }

    private ScheduleMatchResponse mapToResponse(Match match){
        return new ScheduleMatchResponse(
                match.getId(),
                match.getTournament().getId(),
                match.getTournament().getName(),

                match.getTeamA().getId(),
                match.getTeamA().getName(),

                match.getTeamB().getId(),
                match.getTeamB().getName(),
                match.getMatchDate(),
                match.getMatchTime(),
                match.getVenue()
                );
    }


}
