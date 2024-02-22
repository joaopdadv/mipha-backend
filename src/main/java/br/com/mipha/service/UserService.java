package br.com.mipha.service;

import br.com.mipha.entity.user.User;
import br.com.mipha.entity.user.UserPatchRequestDTO;
import br.com.mipha.entity.user.UserRequestDTO;
import br.com.mipha.entity.user.UserResponseDTO;
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

        public List<UserResponseDTO> getAllUsers(){
            List<User> users = userRepository.findAll();

            return users.stream()
                    .map(e -> entityToResponse(e))
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
                User user = requestToEntity(requestDTO);
                userRepository.save(user);
                return entityToResponse(user);
            }
        }

        public UserResponseDTO editUser(String id, UserPatchRequestDTO request) {
            try{
                User user = userRepository.findById(id).get();

                user = patchToEntity(request,  user);
                userRepository.save(user);

                UserResponseDTO response = entityToResponse(user);

                return response;
            }catch (NoSuchElementException e){
                return null;
            }

        }

        public Boolean deleteUser(String id){
            try {
                Optional<User> user = userRepository.findById(id);
                if(user.isPresent()){
                    userRepository.deleteById(id);
                    return true;
                }
                return false;
            }catch (IllegalArgumentException e){
                return false;
            }
        }

        private User patchToEntity(UserPatchRequestDTO request, User user){
            User response = user;

            response.setName(request.getName());
            response.setLastName(request.getLastName());

            return response;
        }

        private UserResponseDTO entityToResponse(User user){
            UserResponseDTO responseDTO = new UserResponseDTO();
            responseDTO.setId(user.getId());
            responseDTO.setName(user.getName());
            responseDTO.setLastName(user.getLastName());
            responseDTO.setEmail(user.getEmail());
            responseDTO.setTeams(user.getTeams());

            return responseDTO;
        }

        private User requestToEntity(UserRequestDTO requestDTO){

            User user = new User();

            user.setName(requestDTO.getName());
            user.setLastName(requestDTO.getLastName());
            user.setEmail(requestDTO.getEmail());
            user.setPassword(requestDTO.getPassword());

            return user;
        }


}
