package br.com.mipha.controller;

import br.com.mipha.entity.user.User;
import br.com.mipha.entity.user.UserRequestDTO;
import br.com.mipha.entity.user.UserResponseDTO;
import br.com.mipha.service.UserService;
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
@RequestMapping("/user")
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

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

}
