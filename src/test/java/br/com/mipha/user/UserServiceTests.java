package br.com.mipha.user;

import br.com.mipha.entity.team.Team;
import br.com.mipha.entity.team.TeamNoUsersResponseDTO;
import br.com.mipha.entity.user.User;
import br.com.mipha.entity.user.UserPutRequestDTO;
import br.com.mipha.entity.user.UserRequestDTO;
import br.com.mipha.entity.user.UserResponseDTO;
import br.com.mipha.repository.TeamRepository;
import br.com.mipha.repository.UserRepository;
import br.com.mipha.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllUsersTest(){
        List<TeamNoUsersResponseDTO> mockTeamsNoUsers = Arrays.asList(
                new TeamNoUsersResponseDTO("A", "PDI DEVS"),
                new TeamNoUsersResponseDTO("B", "MECATRÔNICA")
        );
        List<TeamNoUsersResponseDTO> mockTeamsNoUsers2 = Arrays.asList(
                new TeamNoUsersResponseDTO("B", "MECATRÔNICA")
        );


        List<UserResponseDTO> mockUserResponses = Arrays.asList(
                new UserResponseDTO("1", "João Pedro", "De Villa", "joaopdadv@gmail.com", mockTeamsNoUsers),
                new UserResponseDTO("2", "Arthur", "Silva", "silba@gmail.com", mockTeamsNoUsers2)
        );

        List<User> mockUsers = new ArrayList<>();

        List<Team> mockTeams = Arrays.asList(
                new Team("A", "PDI DEVS", new User(), mockUsers),
                new Team("B", "MECATRÔNICA",  new User(), mockUsers)
        );
        List<Team> mockTeams2 = Arrays.asList(
                new Team("B", "MECATRÔNICA", new User(), mockUsers)
        );

        mockUsers = Arrays.asList(
                new User("1", "João Pedro", "De Villa", "joaopdadv@gmail.com", "123", mockTeams),
                new User("2", "Arthur", "Silva", "silba@gmail.com", "123", mockTeams2)
        );

        when(userRepository.findAll()).thenReturn(mockUsers);

        List<UserResponseDTO> response = userService.getAllUsers();

        assertEquals(mockUserResponses, response);
    }

    @Test
    public void createUserTest(){

        List<TeamNoUsersResponseDTO> mockTeams = new ArrayList<>();
        UserResponseDTO mockResponse = new UserResponseDTO(null, "João Pedro", "De Villa", "joaopdadv@gmail.com", mockTeams);

        UserRequestDTO mockRequest = new UserRequestDTO("João Pedro", "De Villa", "joaopdadv@gmail.com", "123");

        UserResponseDTO response = userService.createUser(mockRequest);
        assertEquals(mockResponse, response);
    }

    @Test
    public void editUserTest(){
        List<TeamNoUsersResponseDTO> mockTeamsNoUser = new ArrayList<>();
        UserResponseDTO mockResponse = new UserResponseDTO("1", "Arthur", "Silba", "joaopdadv@gmail.com", mockTeamsNoUser);

        List<Team> mockTeams = new ArrayList<>();
        Optional<User> mockUser = Optional.of(new User("1", "João Pedro", "De Villa", "joaopdadv@gmail.com", "123", mockTeams));
        UserPutRequestDTO mockRequest = new UserPutRequestDTO("Arthur", "Silba");

        when(userRepository.findById("1")).thenReturn(mockUser);

        UserResponseDTO response = userService.editUser("1", mockRequest);

        assertEquals(response, mockResponse);

    }

    @Test
    public void deleteUserTest(){

        List<Team> mockTeams = new ArrayList<>();
        Optional<User> mockUser = Optional.of(new User("1", "João Pedro", "De Villa", "joaopdadv@gmail.com", "123", mockTeams));

        mockTeams = Arrays.asList(
                new Team("A", "PDI DEVS", mockUser.get(), Arrays.asList(mockUser.get(), new User("2", "Arthur", "Silba", "silba@gmail.com", "123", mockTeams))),
                new Team("B", "MECA", mockUser.get(), Arrays.asList(mockUser.get()))
        );
        mockUser = Optional.of(new User("1", "João Pedro", "De Villa", "joaopdadv@gmail.com", "123", mockTeams));

        when(userRepository.findById("1")).thenReturn(mockUser);
        when(teamRepository.findById("A")).thenReturn(Optional.ofNullable(mockTeams.get(0)));
        when(teamRepository.findById("B")).thenReturn(Optional.ofNullable(mockTeams.get(1)));

        Boolean response = userService.deleteUser("1");

        assertTrue(response);
    }
}
