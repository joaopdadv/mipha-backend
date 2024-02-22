package br.com.mipha.controller;

import br.com.mipha.entity.team.Team;
import br.com.mipha.entity.team.TeamRequestDTO;
import br.com.mipha.entity.team.TeamResponseDTO;
import br.com.mipha.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @GetMapping
    public ResponseEntity<List<TeamResponseDTO>> getAll(){

        List<TeamResponseDTO> teams = teamService.getAll();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(teams);
    }


    @PostMapping
    public ResponseEntity<TeamResponseDTO> createTeam(
            @RequestBody TeamRequestDTO requestDTO
            ){

        TeamResponseDTO team = teamService.createTeam(requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(team);
    }
}
