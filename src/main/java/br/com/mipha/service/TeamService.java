package br.com.mipha.service;

import br.com.mipha.entity.team.Team;
import br.com.mipha.entity.team.TeamRequestDTO;
import br.com.mipha.entity.team.TeamResponseDTO;
import br.com.mipha.entity.user.User;
import br.com.mipha.entity.user.UserResponseDTO;
import br.com.mipha.repository.TeamRepository;
import br.com.mipha.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public List<TeamResponseDTO> getAll(){
        List<Team> teams = teamRepository.findAll();

        return teams.stream()
                .map(e -> teamEntityToResponse(e))
                .collect(Collectors.toList());
    }

    public TeamResponseDTO createTeam(TeamRequestDTO request){
        Team team = teamRequestToEntity(request);
        teamRepository.save(team);

        TeamResponseDTO response = teamEntityToResponse(team);
        return response;
    }

    private Team teamRequestToEntity(TeamRequestDTO request){
        Team team = new Team();
        User owner = userRepository.findById(request.getOwnerId()).get();

        team.setName(request.getName());
        team.setOwner(owner);

        return team;
    }

    private TeamResponseDTO teamEntityToResponse(Team team){
        TeamResponseDTO response = new TeamResponseDTO();

        response.setId(team.getId());
        response.setName(team.getName());
        response.setOwner(userService.userEntityToResponse(team.getOwner()));
        response.setUsers(team.getUsers()
                .stream()
                .map(e -> userService.userEntityToResponse(e))
                .collect(Collectors.toList()));

        return response;
    }


}
