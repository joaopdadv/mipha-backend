package br.com.mipha.service;

import br.com.mipha.entity.team.Team;
import br.com.mipha.entity.team.TeamNoUsersResponseDTO;
import br.com.mipha.entity.user.*;
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
public class UserService {

        private final UserRepository userRepository;
        private final TeamRepository teamRepository;

        public List<UserResponseDTO> getAllUsers(){
            List<User> users = userRepository.findAll();

            return users.stream()
                    .map(e -> userEntityToResponse(e))
                    .collect(Collectors.toList());
        }

        public UserResponseDTO createUser(UserRequestDTO requestDTO) {

            if (requestDTO == null || requestDTO.getName() == null ||
                    requestDTO.getLastName() == null ||
                    requestDTO.getEmail() == null ||
                    requestDTO.getPassword() == null) {
                return null;
            }

            Optional<User> isUserInDB = userRepository.findByEmail(requestDTO.getEmail());

            if (isUserInDB.isPresent()) {
                return null;
            } else {
                User user = userRequestToEntity(requestDTO);
                userRepository.save(user);
                return userEntityToResponse(user);
            }
        }

        public UserResponseDTO editUser(String id, UserPutRequestDTO request) {
            try{
                User user = userRepository.findById(id).get();

                user = userPutToEntity(request,  user);
                userRepository.save(user);

                UserResponseDTO response = userEntityToResponse(user);

                return response;
            }catch (NoSuchElementException e){
                return null;
            }

        }

        public Boolean deleteUser(String id){
            try {
                Optional<User> user = userRepository.findById(id);
                if(user.isPresent()){


                    List<Team> teams = teamRepository.findByOwner(user.get().getId());

                    //Deletes or changes the owner of the user's owned teams
                    teams.forEach(e -> {
                        if(e.getUsers().isEmpty()){
                            teamRepository.deleteById(e.getId());
                        }else{
                            List<User> users = e.getUsers();

                            if (users.size() == 1 && users.get(0).getId().equals(user.get().getId())) {
                                teamRepository.deleteById(e.getId());
                            } else {
                                List<User> usersWithoutDeleted = users.stream()
                                        .filter(u -> !u.getId().equals(user.get().getId()))
                                        .collect(Collectors.toList());
                                e.setUsers(usersWithoutDeleted);

                                User owner = usersWithoutDeleted.stream().findAny().get();

                                e.setOwner(owner);
                                teamRepository.save(e);
                            }
                        }
                    });

                    userRepository.deleteById(id);
                    return true;
                }
                return false;
            }catch (IllegalArgumentException e){
                return false;
            }
        }

        private User userPutToEntity(UserPutRequestDTO request, User user){
            User response = user;

            response.setName(request.getName());
            response.setLastName(request.getLastName());

            return response;
        }

        public UserResponseDTO userEntityToResponse(User user){
            UserResponseDTO responseDTO = new UserResponseDTO();
            responseDTO.setId(user.getId());
            responseDTO.setName(user.getName());
            responseDTO.setLastName(user.getLastName());
            responseDTO.setEmail(user.getEmail());
            responseDTO.setTeams(user.getTeams().stream()
                    .map(e -> teamEntityToResponseNoUsers((e)))
                    .collect(Collectors.toList()));

            return responseDTO;
        }

        private User userRequestToEntity(UserRequestDTO requestDTO){

            User user = new User();

            user.setName(requestDTO.getName());
            user.setLastName(requestDTO.getLastName());
            user.setEmail(requestDTO.getEmail());
            user.setPassword(requestDTO.getPassword());

            return user;
        }

        private TeamNoUsersResponseDTO teamEntityToResponseNoUsers(Team team){
            TeamNoUsersResponseDTO response = new TeamNoUsersResponseDTO();

            response.setId(team.getId());
            response.setName(team.getName());

            return response;
        }
}
