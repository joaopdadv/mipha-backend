package br.com.mipha.service;

import br.com.mipha.entity.team.Team;
import br.com.mipha.entity.team.TeamNoUsersResponseDTO;
import br.com.mipha.entity.team.TeamRequestDTO;
import br.com.mipha.entity.team.TeamResponseDTO;
import br.com.mipha.entity.user.User;
import br.com.mipha.entity.user.UserNoTeamsResponseDTO;
import br.com.mipha.repository.TeamRepository;
import br.com.mipha.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public List<TeamResponseDTO> getAll(){
        List<Team> teams = teamRepository.findAll();

        return teams.stream()
                .map(e -> teamEntityToResponse(e))
                .collect(Collectors.toList());
    }

    public TeamResponseDTO createTeam(TeamRequestDTO request){
        Team team = teamRequestToEntity(request);

        if(team != null){
            User owner = userRepository.findById(team.getOwner().getId()).get();
            List<Team> teams = owner.getTeams();
            teams.add(team);
            owner.setTeams(teams);

            teamRepository.save(team);
            userRepository.save(owner);

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

    public TeamResponseDTO addUser(String idTeam, String idUser) {
        Optional<Team> teamExists = teamRepository.findById(idTeam);
        Optional<User> userExists = userRepository.findById(idUser);

        if(teamExists.isPresent() && userExists.isPresent()){

            Team team = teamExists.get();
            User user = userExists.get();

            List<User> teamUsers = team.getUsers();
            List<Team> userTeams = user.getTeams();

            boolean userAlreadyExists = teamUsers.stream()
                    .anyMatch(u -> u.getId().equals(user.getId()));
            boolean teamAlreadyExists = userTeams.stream()
                    .anyMatch(t -> t.getId().equals(team.getId()));

            if(userAlreadyExists && teamAlreadyExists){
                return null;
            }

            if (!userAlreadyExists) {
                teamUsers.add(user);
                team.setUsers(teamUsers);
                teamRepository.save(team);
            }

            if (!teamAlreadyExists) {
                userTeams.add(team);
                user.setTeams(userTeams);
                userRepository.save(user);
            }

            return teamEntityToResponse(team);
        }
        return null;
    }

    public Boolean deleteTeam(String id) {

        try{
            Optional<Team> team = teamRepository.findById(id);

            if(team.isPresent()){
                List<User> users = team.get().getUsers();

                for(User user : users){
                    List<Team> teams = user.getTeams();

                    user.setTeams(teams.stream()
                            .filter(t -> !t.getId().equals(team.get().getId()))
                            .collect(Collectors.toList()));

                    userRepository.save(user);
                }

                teamRepository.deleteById(id);
            }
            return team.isPresent();
        }catch (IllegalArgumentException e){
            return false;
        }
    }

    private Team teamRequestToEntity(TeamRequestDTO request){
        Team team = new Team();
        Optional<User> owner = userRepository.findById(request.getOwnerId());

        if(owner.isPresent()){
            List<User> users = new ArrayList<>();
            users.add(owner.get());


            team.setOwner(owner.get());
            team.setName(request.getName());
            team.setUsers(users);
            return team;
        }

        return null;
    }

    private TeamResponseDTO teamEntityToResponse(Team team){
        TeamResponseDTO response = new TeamResponseDTO();

        response.setId(team.getId());
        response.setName(team.getName());
        response.setOwner(userEntityToNoTeamResponse(team.getOwner()));
        response.setUsers(team.getUsers()
                .stream()
                .map(e -> userEntityToNoTeamResponse(e))
                .collect(Collectors.toList()));

        return response;
    }

    private UserNoTeamsResponseDTO userEntityToNoTeamResponse(User user) {
        UserNoTeamsResponseDTO responseDTO = new UserNoTeamsResponseDTO();
        responseDTO.setId(user.getId());
        responseDTO.setName(user.getName());
        responseDTO.setLastName(user.getLastName());
        responseDTO.setEmail(user.getEmail());

        return responseDTO;
    }


}
