package com.example.smart_cricket_tournament.service;

import com.example.smart_cricket_tournament.dto.*;
import com.example.smart_cricket_tournament.entity.*;
import com.example.smart_cricket_tournament.enums.MatchStatus;
import com.example.smart_cricket_tournament.exception.BadRequestException;
import com.example.smart_cricket_tournament.exception.ResourceNotFoundException;
import com.example.smart_cricket_tournament.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MatchService {
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;
    private final PointsTableRepository pointsTableRepository;
    private final PointsTableService pointsTableService;
    private final PlayerRepository playerRepository;
    private final LiveMatchPlayerStatsRepository liveMatchPlayerStatsRepository;

    public ScheduleMatchResponse scheduleMatch(ScheduleMatchRequest request) {

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

        Match savedMatch = matchRepository.save(match);

        return mapToResponse(savedMatch);
    }

    public List<ScheduleMatchResponse> getMatchesByTournament(Long tournamentId) {
        List<Match> matches = matchRepository.findByTournamentId(tournamentId);
        return matches.stream().map(this::mapToResponse).toList();
    }

    public Match updateMatchResult(Long matchId, MatchResultRequest request) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found"));

        if (match.getStatus() == MatchStatus.COMPLETED) {
            throw new BadRequestException("Match result is already updated");
        }

        Team winner = teamRepository.findById(request.getWinnerTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Winner team not found"));

        Team loser = (winner.getId().equals(match.getTeamA().getId())) ? match.getTeamB() : match.getTeamA();

        match.setWinner(winner);
        match.setStatus(MatchStatus.COMPLETED);
        match.setTeamARuns(request.getTeamARuns());
        match.setTeamAOvers(request.getTeamAOvers());
        match.setTeamBRuns(request.getTeamBRuns());
        match.setTeamBOvers(request.getTeamBOvers());

        Match updatedMatch = matchRepository.save(match);
        pointsTableService.updatePointsAfterMatch(
                match.getTeamA(),
                match.getTeamB(),
                match.getTeamARuns(),
                match.getTeamAOvers(),
                match.getTeamBRuns(),
                match.getTeamBOvers(),
                match.getWinner(),
                match.getTournament()
        );
        return updatedMatch;

    }

    public Match updateLiveScore(Long matchId, LiveScoreUpdateDetailedRequest request) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found"));

        if (match.getStatus() == MatchStatus.COMPLETED) {
            throw new BadRequestException("Match already completed");
        }

        match.setTeamARuns(request.teamARuns());
        match.setTeamAOvers(request.teamAOvers());
        match.setTeamAWickets(request.teamAWickets());
        match.setTeamBRuns(request.teamBRuns());
        match.setTeamBOvers(request.teamBOvers());
        match.setTeamBWickets(request.teamBWickets());

        matchRepository.save(match);
        updatePlayerStats(matchId, request.strikerId(), request.strikerRuns(), request.strikerBalls(), true, false, false);
        updatePlayerStats(matchId, request.nonStrikerId(), request.nonStrikerRuns(), request.nonStrikerBalls(), false, true, false);
        updatePlayerStats(matchId, request.bowlerId(), 0, 0, false, false, true, request.bowlerWickets(), request.bowlerOvers());

        return match;
    }
    private void updatePlayerStats(Long matchId, Long playerId, Integer runs, Integer balls, boolean striker, boolean nonStriker, boolean bowler) {
        updatePlayerStats(matchId, playerId, runs, balls, striker, nonStriker, bowler, 0, 0);
    }

    private void updatePlayerStats(Long matchId, Long playerId, Integer runs, Integer balls, boolean striker, boolean nonStriker, boolean bowler, Integer wickets, Integer overs) {
        if (playerId == null) return;

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found"));

        LiveMatchPlayerStats stats = liveMatchPlayerStatsRepository
                .findByMatchIdAndPlayerId(matchId, playerId)
                .orElseGet(LiveMatchPlayerStats::new);

        stats.setMatch(Match.builder().id(matchId).build());
        stats.setPlayer(player);

        if (striker) stats.setStriker(true);
        if (nonStriker) stats.setNonStriker(true);
        if (bowler) stats.setBowler(true);

        stats.setRunsScored(runs);
        stats.setBallsFaced(balls);
        stats.setWicketsTaken(wickets);
        stats.setOversBowled(overs);

        liveMatchPlayerStatsRepository.save(stats);
    }

    public ScheduleMatchResponse mapToResponse(Match match){
        return ScheduleMatchResponse.builder()
                .matchId(match.getId())
                .tournamentId(match.getTournament().getId())
                .tournamentName(match.getTournament().getName())
                .teamAId(match.getTeamA().getId())
                .teamAName(match.getTeamA().getName())
                .teamBId(match.getTeamB().getId())
                .teamBName(match.getTeamB().getName())
                .matchDate(match.getMatchDate())
                .matchTime(match.getMatchTime())
                .venue(match.getVenue())

                // Live scores
                .teamARuns(match.getTeamARuns())
                .teamAOvers(match.getTeamAOvers())
                .teamBRuns(match.getTeamBRuns())
                .teamBOvers(match.getTeamBOvers())

                // Run Rates
                .teamARunRate(calculateRunRate(match.getTeamARuns(), match.getTeamAOvers()))
                .teamBRunRate(calculateRunRate(match.getTeamBRuns(), match.getTeamBOvers()))

                .build();
    }

    private Double calculateRunRate(Integer runs, Integer overs) {
        if (runs == null || overs == null || overs == 0) return 0.0;
        return (double) runs / overs;
    }


    //    private ScheduleMatchResponse mapToResponse(Match match){
//        return new ScheduleMatchResponse(
//                match.getId(),
//                match.getTournament().getId(),
//                match.getTournament().getName(),
//
//                match.getTeamA().getId(),
//                match.getTeamA().getName(),
//
//                match.getTeamB().getId(),
//                match.getTeamB().getName(),
//                match.getMatchDate(),
//                match.getMatchTime(),
//                match.getVenue()
//                );
//    }


}
