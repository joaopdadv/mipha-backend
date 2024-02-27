package br.com.mipha.controller;

import br.com.mipha.entity.user.UserLoginRequest;
import br.com.mipha.entity.user.UserRegisterRequest;
import br.com.mipha.service.AuthorizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthorizationService authorizationService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid UserLoginRequest userLoginRequest){
        return authorizationService.login(userLoginRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register (@RequestBody UserRegisterRequest registerDto){
        return authorizationService.register(registerDto);
    }
}
