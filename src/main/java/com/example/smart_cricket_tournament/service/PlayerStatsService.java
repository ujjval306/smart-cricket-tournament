package com.example.smart_cricket_tournament.service;

import com.example.smart_cricket_tournament.dto.PlayerMatchFormDTO;
import com.example.smart_cricket_tournament.entity.Match;
import com.example.smart_cricket_tournament.entity.Player;
import com.example.smart_cricket_tournament.entity.PlayerMatchStats;
import com.example.smart_cricket_tournament.repository.PlayerMatchStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerStatsService {
    private final PlayerMatchStatsRepository playerMatchStatsRepository;

    public List<PlayerMatchFormDTO> getLast5MatchesForm(Player player) {
        List<PlayerMatchStats> stats = playerMatchStatsRepository.findTop5ByPlayerOrderByMatch_MatchDateDesc(player);

        return stats.stream().map(stat -> {
            Match match = stat.getMatch();
            String opponent = getOpponentTeamName(stat);
            return new PlayerMatchFormDTO(
                    match.getId(),
                    match.getMatchDate().toString(),
                    opponent,
                    stat.getRunsScored(),
                    stat.getBallsFaced(),
                    stat.getWicketsTaken(),
                    stat.getOversBowled()
            );
        }).toList();
    }
    private String getOpponentTeamName(PlayerMatchStats stat) {
        String teamA = stat.getMatch().getTeamA().getName();
        String teamB = stat.getMatch().getTeamB().getName();
        String playerTeam = stat.getPlayer().getTeam().getName();

        return playerTeam.equals(teamA) ? teamB : teamA;
    }
}
