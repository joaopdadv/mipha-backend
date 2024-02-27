package br.com.mipha.service;

import br.com.mipha.config.TokenService;
import br.com.mipha.entity.user.*;
import br.com.mipha.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorizationService implements UserDetailsService {


    private final ApplicationContext context;

    private final UserRepository userRepository;

    private final TokenService tokenService;
    private final UserService userService;

    private AuthenticationManager authenticationManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userOptional = userRepository.findByEmail(username);
        if (userOptional.isPresent()){
            return userOptional.get();
        }

        return null;
    }

    public ResponseEntity<Object> login(@RequestBody @Valid UserLoginRequest data){
        authenticationManager = context.getBean(AuthenticationManager.class);

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());

        Optional<User> user = userRepository.findByEmail(data.getEmail());

        if(user.isPresent()){
            return ResponseEntity.ok(new UserLoginResponse(token, userService.userEntityToResponse(user.get())));
        }
        return ResponseEntity.notFound().build();
    }


    public ResponseEntity<Object> register (@RequestBody UserRegisterRequest registerDto){
        if (this.userRepository.findByEmail(registerDto.getEmail()).isPresent()) return ResponseEntity.badRequest().build();
        String encryptedPassword = new BCryptPasswordEncoder().encode(registerDto.getPassword());

        User newUser = userRegisterToEntity(registerDto, encryptedPassword);

        this.userRepository.save(newUser);
        return ResponseEntity.ok().build();
    }

    private User userRegisterToEntity(UserRegisterRequest requestDTO, String pass){

        User user = new User();

        user.setName(requestDTO.getName());
        user.setLastName(requestDTO.getLastName());
        user.setEmail(requestDTO.getEmail());
        user.setPassword(pass);
        user.setRole(requestDTO.getRole());

        return user;
    }
}
