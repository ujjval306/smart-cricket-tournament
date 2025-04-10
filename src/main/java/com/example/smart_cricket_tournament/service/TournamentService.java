package com.example.smart_cricket_tournament.service;

import com.example.smart_cricket_tournament.dto.TournamentRequest;
import com.example.smart_cricket_tournament.dto.TournamentResponse;
import com.example.smart_cricket_tournament.entity.Tournament;
import com.example.smart_cricket_tournament.exception.AccessDeniedException;
import com.example.smart_cricket_tournament.exception.BadRequestException;
import com.example.smart_cricket_tournament.repository.TournamentRepository;
import com.example.smart_cricket_tournament.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class TournamentService  {
    private final TournamentRepository tournamentRepository;
    private final JwtService jwtService;
    private final HttpServletRequest httpServletRequest;


    public TournamentResponse createTournament(TournamentRequest request ){

        String authHeader = httpServletRequest.getHeader("Authorization");
        System.out.println(authHeader);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid token");
        }

        String token = authHeader.substring(7);
        System.out.println("token" + token);
        String role = jwtService.extractUserRole(token);
        System.out.println("Role" + role);

        if (!role.equals("ADMIN") && !role.equals("TEAM_MANAGER")) {
            throw new AccessDeniedException( "You are not allowed to create a tournament");
        }

        boolean exist = tournamentRepository.existsByName(request.name());
        if(exist){
            throw new BadRequestException("Tournament with this name already exist");
        }

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
