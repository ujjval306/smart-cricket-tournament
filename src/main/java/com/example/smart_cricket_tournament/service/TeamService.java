package com.example.smart_cricket_tournament.service;

import com.example.smart_cricket_tournament.dto.TeamRequest;
import com.example.smart_cricket_tournament.dto.TeamResponse;
import com.example.smart_cricket_tournament.entity.Team;
import com.example.smart_cricket_tournament.entity.Tournament;
import com.example.smart_cricket_tournament.exception.BadRequestException;
import com.example.smart_cricket_tournament.repository.TeamRepository;
import com.example.smart_cricket_tournament.repository.TournamentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final TournamentRepository tournamentRepository;

    public TeamResponse createTeam(TeamRequest request){
        Tournament tournament = tournamentRepository.findById(request.tournamentId())
                .orElseThrow(()->new RuntimeException("Tournament not found"));

        boolean exists = teamRepository.existsByNameAndTournamentId(request.name(), request.tournamentId());
        if (exists) {
            throw new BadRequestException("Team with this name already exists in the tournament");
        }

        Team team = Team.builder()
                .name(request.name())
//                .city(request.city())
                .tournament(tournament)
                .build();
        return mapToResponse(teamRepository.save(team));

    }

    public List<TeamResponse> getAllTeams(){
        return teamRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    public TeamResponse getTeam(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        return mapToResponse(team);
    }

    public TeamResponse updateTeam(Long id, TeamRequest request) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        Tournament tournament = tournamentRepository.findById(request.tournamentId())
                .orElseThrow(() -> new RuntimeException("Tournament not found"));

//        if (!team.getName().equalsIgnoreCase(request.name()) || !team.getTournament().getId().equals(request.tournamentId())) {
//            boolean exists = teamRepository.existsByNameAndTournamentId(request.name(), request.tournamentId());
//            if (exists) {
//                throw new BadRequestException("Another team with this name already exists in the tournament");
//            }
//        }


        team.setName(request.name());
//        team.setCity(request.city());
        team.setTournament(tournament);

        return mapToResponse(teamRepository.save(team));
    }

    public void deleteTeam(Long id) {
        teamRepository.deleteById(id);
    }

    private TeamResponse mapToResponse(Team team) {
        return new TeamResponse(
                team.getId(),
                team.getName(),
                team.getCity(),
                team.getTournament().getId(),
                team.getTournament().getName()
        );
    }
}
