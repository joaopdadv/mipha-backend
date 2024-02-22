package br.com.mipha.controller;

import br.com.mipha.entity.team.TeamRequestDTO;
import br.com.mipha.entity.team.TeamResponseDTO;
import br.com.mipha.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

        if(team != null){
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(team);
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamResponseDTO> editTeam(
            @PathVariable String id,
            @RequestBody TeamRequestDTO request
            ){

        TeamResponseDTO response = teamService.editTeam(request, id);

        if(response != null){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response);
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .build();
    }

    @PutMapping("/{idTeam}/{idUser}")
    public ResponseEntity<TeamResponseDTO> addUserToTeam(
            @PathVariable String idTeam,
            @PathVariable String idUser
    ){

        TeamResponseDTO response = teamService.addUser(idTeam, idUser);

        return null;
    }
}
