package br.com.mipha.service;

import br.com.mipha.entity.team.Team;
import br.com.mipha.entity.team.TeamRequestDTO;
import br.com.mipha.entity.team.TeamResponseDTO;
import br.com.mipha.entity.user.User;
import br.com.mipha.repository.TeamRepository;
import br.com.mipha.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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

        if(team != null){
            teamRepository.save(team);

            TeamResponseDTO response = teamEntityToResponse(team);
            return response;
        }

        return null;
    }

    public TeamResponseDTO editTeam(TeamRequestDTO request, String id) {

        try{
            Team team = teamRepository.findById(id).get();
            Optional<User> owner = userRepository.findById(request.getOwnerId());

            if(owner.isPresent()){
                team.setName(request.getName());
                team.setOwner(owner.get());

                teamRepository.save(team);

                return teamEntityToResponse(team);
            }

            return null;
        }catch (NoSuchElementException e){
            return null;
        }
    }

    private Team teamRequestToEntity(TeamRequestDTO request){
        Team team = new Team();
        Optional<User> owner = userRepository.findById(request.getOwnerId());

        if(owner.isPresent()){
            team.setOwner(owner.get());
            team.setName(request.getName());
            return team;
        }

        return null;
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
