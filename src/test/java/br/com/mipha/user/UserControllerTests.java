package br.com.mipha.user;

import br.com.mipha.controller.TeamController;
import br.com.mipha.controller.UserController;
import br.com.mipha.entity.team.TeamNoUsersResponseDTO;
import br.com.mipha.entity.user.UserPutRequestDTO;
import br.com.mipha.entity.user.UserRequestDTO;
import br.com.mipha.entity.user.UserResponseDTO;
import br.com.mipha.service.TeamService;
import br.com.mipha.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserControllerTests {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAll(){
        List<TeamNoUsersResponseDTO> mockTeams = Arrays.asList(
                new TeamNoUsersResponseDTO("A", "PDI DEVS"),
                new TeamNoUsersResponseDTO("B", "MECATRÔNICA")
        );
        List<TeamNoUsersResponseDTO> mockTeams2 = Arrays.asList(
                new TeamNoUsersResponseDTO("B", "MECATRÔNICA")
        );


        List<UserResponseDTO> mockUsers = Arrays.asList(
                new UserResponseDTO("1", "João Pedro", "De Villa", "joaopdadv@gmail.com", mockTeams),
                new UserResponseDTO("2", "Arthur", "Silva", "silba@gmail.com", mockTeams2)
        );
        when(userService.getAllUsers()).thenReturn(mockUsers);

        ResponseEntity<List<UserResponseDTO>> responseEntity = userController.getAllUsers();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        List<UserResponseDTO> responseBody = responseEntity.getBody();
        assertEquals(new UserResponseDTO("1", "João Pedro", "De Villa", "joaopdadv@gmail.com", mockTeams), responseBody.get(0));
        assertEquals(new UserResponseDTO("2", "Arthur", "Silva", "silba@gmail.com", mockTeams2), responseBody.get(1));
    }

    @Test
    public void testCreateUser(){
        UserRequestDTO mockRequest = new UserRequestDTO("João", "De Villa", "joaopdadv@gmail.com", "123");
        UserResponseDTO mockResponse = new UserResponseDTO("1", "João", "De Villa", "joaopdadv@gmail.com", new ArrayList<>());

        when(userService.createUser(mockRequest)).thenReturn(mockResponse);

        ResponseEntity<UserResponseDTO> responseEntity = userController.createUser(mockRequest);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(new UserResponseDTO("1", "João", "De Villa", "joaopdadv@gmail.com", new ArrayList<>()), responseEntity.getBody());
    }

    @Test
    public void testEditUser(){
        UserPutRequestDTO mockRequest = new UserPutRequestDTO("João", "De Villa");
        UserResponseDTO mockResponse = new UserResponseDTO("1", "João", "De Villa", "joaopdadv@gmail.com", new ArrayList<>());

        when(userService.editUser("1", mockRequest)).thenReturn(mockResponse);

        ResponseEntity<UserResponseDTO> responseEntity = userController.editUser("1", mockRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(new UserResponseDTO("1", "João", "De Villa", "joaopdadv@gmail.com", new ArrayList<>()), responseEntity.getBody());
    }

    @Test
    public void testDeleteUser(){
        when(userService.deleteUser("1")).thenReturn(true);
        when(userService.deleteUser("2")).thenReturn(false);

        ResponseEntity responseEntity = userController.deleteUser("1");
        ResponseEntity responseEntity2 = userController.deleteUser("2");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity2.getStatusCode());
    }
}
