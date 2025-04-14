package com.example.smart_cricket_tournament.service;

import com.example.smart_cricket_tournament.entity.PointsTable;
import com.example.smart_cricket_tournament.entity.Team;
import com.example.smart_cricket_tournament.entity.Tournament;
import com.example.smart_cricket_tournament.repository.PointsTableRepository;
import com.example.smart_cricket_tournament.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointsTableService {
    private final PointsTableRepository pointsTableRepository;
    private final TournamentRepository tournamentRepository;

    public List<PointsTable> getPointsTableForTournament (Long tournamentId) {
        return pointsTableRepository.findAllByTournamentId(tournamentId);
    }

    public void updatePointsAfterMatch(  Team teamA, Team teamB,
                                         int teamARuns, double teamAOvers,
                                         int teamBRuns, double teamBOvers,
                                         Team winningTeam,
                                         Tournament tournament) {
        updateStats(teamA, tournament, teamA.equals(winningTeam), teamARuns, teamAOvers, teamBRuns, teamBOvers);
        updateStats(teamB, tournament, teamB.equals(winningTeam), teamBRuns, teamBOvers, teamARuns, teamAOvers);

    }

    private void updateStats(Team team, Tournament tournament, boolean isWin,
                             int runsScored, double oversFaced,
                             int runsConceded, double oversBowled) {

        PointsTable table = pointsTableRepository.findByTeamAndTournament(team, tournament)
                .orElse(new PointsTable(null, team, tournament, 0, 0, 0, 0, 0, 0,
                        0, 0.0, 0, 0.0, 0.0));

        table.setMatchesPlayed(table.getMatchesPlayed() + 1);
        table.setRunsScored(table.getRunsScored() + runsScored);
        table.setOversFaced(table.getOversFaced() + oversFaced);
        table.setRunsConceded(table.getRunsConceded() + runsConceded);
        table.setOversBowled(table.getOversBowled() + oversBowled);

        if(isWin){
            table.setWins(table.getWins() + 1);
            table.setPoints(table.getPoints() + 2);
        }else{
            table.setLosses(table.getLosses() + 1);
        }

        double rrScored = table.getOversFaced() > 0 ? (double) table.getRunsScored() / table.getOversFaced() : 0.0;
        double rrConceded = table.getOversBowled() > 0 ? (double) table.getRunsConceded() / table.getOversBowled() : 0.0;
        table.setNetRunRate(rrScored - rrConceded);

        pointsTableRepository.save(table);
    }
}
