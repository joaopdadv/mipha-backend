package br.com.mipha.team;

import br.com.mipha.entity.team.Team;
import br.com.mipha.entity.team.TeamRequestDTO;
import br.com.mipha.entity.team.TeamResponseDTO;
import br.com.mipha.entity.user.User;
import br.com.mipha.entity.user.UserNoTeamsResponseDTO;
import br.com.mipha.enums.UserRole;
import br.com.mipha.repository.TeamRepository;
import br.com.mipha.repository.UserRepository;
import br.com.mipha.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.*;

public class TeamServiceTests {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TeamService teamService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllTeamsTest(){

        User user1 = new User("1", "João Pedro", "De Villa", "joaopdadv@gmail.com", "123", new ArrayList<>(), UserRole.ADMIN, new Date(System.currentTimeMillis()));
        User user2 = new User("2", "Arthur", "Silba", "silba@gmail.com", "123", new ArrayList<>(), UserRole.ADMIN, new Date(System.currentTimeMillis()));

        List<Team> mockTeams = Arrays.asList(
                new Team("A", "PDI DEV", user1, Arrays.asList(user1, user2)),
                new Team("B", "MECA", user1, Arrays.asList(user1))
        );

        when(teamRepository.findAll()).thenReturn(mockTeams);

        UserNoTeamsResponseDTO owner = new UserNoTeamsResponseDTO("1", "João Pedro", "De Villa", "joaopdadv@gmail.com");
        UserNoTeamsResponseDTO owner2 = new UserNoTeamsResponseDTO("2", "Arthur", "Silba", "silba@gmail.com");

        List<TeamResponseDTO> mockResponse = Arrays.asList(
                new TeamResponseDTO("A", "PDI DEV", owner, Arrays.asList(owner, owner2)),
                new TeamResponseDTO("B", "MECA", owner, Arrays.asList(owner))
        );

        List<TeamResponseDTO> response = teamService.getAll();

        assertEquals(response, mockResponse);
    }

    @Test
    public void createTeamTest(){
        UserNoTeamsResponseDTO owner = new UserNoTeamsResponseDTO("1", "João Pedro", "De Villa", "joaopdadv@gmail.com");
        User user = new User("1", "João Pedro", "De Villa", "joaopdadv@gmail.com", "123", new ArrayList<>(), UserRole.ADMIN, new Date(System.currentTimeMillis()));

        TeamRequestDTO mockRequest = new TeamRequestDTO("PDI DEV", "1");

        TeamResponseDTO mockResponse = new TeamResponseDTO(null, "PDI DEV", owner, Arrays.asList(owner));

        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        TeamResponseDTO response = teamService.createTeam(mockRequest);

        assertEquals(response, mockResponse);

    }

    @Test
    public void editTeamTest(){

        User user1 = new User("1", "João Pedro", "De Villa", "joaopdadv@gmail.com", "123", new ArrayList<>(), UserRole.ADMIN, new Date(System.currentTimeMillis()));
        User user2 = new User("2", "Arthur", "Silba", "silba@gmail.com", "123", new ArrayList<>(), UserRole.ADMIN, new Date(System.currentTimeMillis()));

        Optional<Team> mockTeam = Optional.of(new Team("A", "MECA", user1, Arrays.asList(user1, user2)));
        TeamRequestDTO mockRequest = new TeamRequestDTO("PDI DEV", "2");

        UserNoTeamsResponseDTO owner = new UserNoTeamsResponseDTO("1", "João Pedro", "De Villa", "joaopdadv@gmail.com");
        UserNoTeamsResponseDTO owner2 = new UserNoTeamsResponseDTO("2", "Arthur", "Silba", "silba@gmail.com");
        TeamResponseDTO mockResponse = new TeamResponseDTO("A", "PDI DEV", owner2, Arrays.asList(owner, owner2));

        when(teamRepository.findById("A")).thenReturn(mockTeam);
        when(userRepository.findById("1")).thenReturn(Optional.of(user1));
        when(userRepository.findById("2")).thenReturn(Optional.of(user2));

        TeamResponseDTO response = teamService.editTeam(mockRequest, "A");

        assertEquals(response, mockResponse);

        //If user is NOT in users list, it cannot be set as owner

        Optional<Team> mockTeam2 = Optional.of(new Team("B", "MECA", user1, Arrays.asList(user1)));
        TeamRequestDTO mockRequest2 = new TeamRequestDTO("PDI DEV", "2");

        when(teamRepository.findById("B")).thenReturn(mockTeam2);
        TeamResponseDTO response2 = teamService.editTeam(mockRequest2, "B");

        assertNull(response2);
    }

    @Test
    public void deleteTeamTest(){

        User user1 = new User("1", "João Pedro", "De Villa", "joaopdadv@gmail.com", "123", new ArrayList<>(), UserRole.ADMIN, new Date(System.currentTimeMillis()));
        User user2 = new User("2", "Arthur", "Silba", "silba@gmail.com", "123", new ArrayList<>(), UserRole.ADMIN, new Date(System.currentTimeMillis()));

        Optional<Team> mockTeam = Optional.of(new Team("A", "MECA", user1, Arrays.asList(user1, user2)));

        when(teamRepository.findById("A")).thenReturn(mockTeam);

        Boolean response = teamService.deleteTeam("A");

        assertTrue(response);
    }
}
