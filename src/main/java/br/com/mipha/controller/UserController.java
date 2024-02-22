package br.com.mipha.controller;

import br.com.mipha.entity.user.UserPutRequestDTO;
import br.com.mipha.entity.user.UserRequestDTO;
import br.com.mipha.entity.user.UserResponseDTO;
import br.com.mipha.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        List<UserResponseDTO> response = userService.getAllUsers();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
            @RequestBody UserRequestDTO user
            ){

        UserResponseDTO response = userService.createUser(user);

        if(response != null){
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);
        }else{
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> editUser(
            @PathVariable String id,
            @RequestBody UserPutRequestDTO request) {

        UserResponseDTO response = userService.editUser(id, request);

        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable String id){
        Boolean delete = userService.deleteUser(id);

        if (delete){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .build();
        }else{
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }
}
