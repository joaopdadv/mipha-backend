package br.com.mipha.service;

import br.com.mipha.entity.user.User;
import br.com.mipha.entity.user.UserRequestDTO;
import br.com.mipha.entity.user.UserResponseDTO;
import br.com.mipha.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

        private final UserRepository userRepository;

        public List<UserResponseDTO> getAllUsers(){
            List<User> users = userRepository.findAll();

            return users.stream()
                    .map(e -> toResponseDto(e))
                    .collect(Collectors.toList());
        }

        public UserResponseDTO createUser(UserRequestDTO requestDTO) {

            User user = requestToEntity(requestDTO);
            userRepository.save(user);
            return toResponseDto(user);
        }

        private UserResponseDTO toResponseDto(User user){
            UserResponseDTO responseDTO = new UserResponseDTO();
            responseDTO.setId(user.getId());
            responseDTO.setName(user.getName());
            responseDTO.setEmail(user.getEmail());
            responseDTO.setTeams(user.getTeams());

            return responseDTO;
        }

        private User requestToEntity(UserRequestDTO requestDTO){

            User user = new User();

            user.setName(requestDTO.getName());
            user.setEmail(requestDTO.getEmail());
            user.setPassword(requestDTO.getPassword());

            return user;
        }

}
