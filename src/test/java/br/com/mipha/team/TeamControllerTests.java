package br.com.mipha.team;

import br.com.mipha.controller.TeamController;
import br.com.mipha.entity.team.TeamRequestDTO;
import br.com.mipha.entity.team.TeamResponseDTO;
import br.com.mipha.entity.user.User;
import br.com.mipha.entity.user.UserNoTeamsResponseDTO;
import br.com.mipha.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeamControllerTests {

    @Mock
    private TeamService teamService;

    @InjectMocks
    private TeamController teamController;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllTeamsTest(){
        UserNoTeamsResponseDTO owner = new UserNoTeamsResponseDTO("1", "João Pedro", "De Villa", "joaopdadv@gmail.com");
        List<TeamResponseDTO> mockResponse = Arrays.asList(
                new TeamResponseDTO("A", "PDI DEV", owner, Arrays.asList(owner))
        );

        when(teamService.getAll()).thenReturn(mockResponse);

        ResponseEntity<List<TeamResponseDTO>> responseEntity = teamController.getAll();

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertEquals(responseEntity.getBody(), mockResponse);
    }

    @Test
    public void createTeam(){
        UserNoTeamsResponseDTO owner = new UserNoTeamsResponseDTO("1", "João Pedro", "De Villa", "joaopdadv@gmail.com");
        TeamResponseDTO mockResponse = new TeamResponseDTO("A", "PDI DEV", owner, Arrays.asList(owner));

        TeamRequestDTO mockRequest = new TeamRequestDTO("PDI DEV", "1");
        when(teamService.createTeam(mockRequest)).thenReturn(mockResponse);

        ResponseEntity<TeamResponseDTO> responseEntity = teamController.createTeam(mockRequest);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
        assertEquals(responseEntity.getBody(), mockResponse);
    }

    @Test
    public void editTeam(){
        UserNoTeamsResponseDTO owner = new UserNoTeamsResponseDTO("1", "João Pedro", "De Villa", "joaopdadv@gmail.com");
        UserNoTeamsResponseDTO user = new UserNoTeamsResponseDTO("2", "Arthur", "Silba", "silba@gmail.com");

        //Here it should work because the user being set to owner is in the group
//        TeamResponseDTO team1 = new TeamResponseDTO("A", "MECA", owner, Arrays.asList(owner, user));
        TeamResponseDTO mockResponse1 = new TeamResponseDTO("A", "PDI DEV", user, Arrays.asList(owner, user));
        TeamRequestDTO mockRequest1 = new TeamRequestDTO("PDI DEV", "2");

        when(teamService.editTeam(mockRequest1, "A")).thenReturn(mockResponse1);

        ResponseEntity<TeamResponseDTO> responseEntity1 = teamController.editTeam("A", mockRequest1);

        //Here it shouldn't work because the user being set to owner is NOT in the group
//        TeamResponseDTO team2 = new TeamResponseDTO("A", "MECA", owner, Arrays.asList(owner));
        TeamRequestDTO mockRequest2 = new TeamRequestDTO("PDI DEV", "2");

        assertEquals(responseEntity1.getStatusCode(), HttpStatus.OK);
        assertEquals(responseEntity1.getBody(), mockResponse1);

        when(teamService.editTeam(mockRequest2, "A")).thenReturn(null);

        ResponseEntity<TeamResponseDTO> responseEntity2 = teamController.editTeam("A", mockRequest2);

        assertEquals(responseEntity2.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void addUserToTeamTest(){

        UserNoTeamsResponseDTO owner = new UserNoTeamsResponseDTO("1", "João Pedro", "De Villa", "joaopdadv@gmail.com");
        UserNoTeamsResponseDTO user = new UserNoTeamsResponseDTO("2", "Arthur", "Silba", "silba@gmail.com");

//        TeamResponseDTO team = new TeamResponseDTO("A", "MECA", owner, Arrays.asList(owner));
        TeamResponseDTO mockResponse = new TeamResponseDTO("A", "PDI DEV", user, Arrays.asList(owner, user));

        when(teamService.addUser("A", "2")).thenReturn(mockResponse);
        when(teamService.addUser("B", "2")).thenReturn(null);
        when(teamService.addUser("A", "3")).thenReturn(null);
        when(teamService.addUser("A", "1")).thenReturn(null);

        ResponseEntity<TeamResponseDTO> response = teamController.addUserToTeam("A", "2");
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), mockResponse);

        ResponseEntity<TeamResponseDTO> response2 = teamController.addUserToTeam("B", "2");
        assertEquals(response2.getStatusCode(), HttpStatus.BAD_REQUEST);

        ResponseEntity<TeamResponseDTO> response3 = teamController.addUserToTeam("A", "3");
        assertEquals(response3.getStatusCode(), HttpStatus.BAD_REQUEST);

        ResponseEntity<TeamResponseDTO> response4 = teamController.addUserToTeam("A", "1");
        assertEquals(response4.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void removeUserFromTeamTest(){
        UserNoTeamsResponseDTO owner = new UserNoTeamsResponseDTO("1", "João Pedro", "De Villa", "joaopdadv@gmail.com");
        UserNoTeamsResponseDTO user = new UserNoTeamsResponseDTO("2", "Arthur", "Silba", "silba@gmail.com");

//        TeamResponseDTO team = new TeamResponseDTO("A", "MECA", owner, Arrays.asList(owner, user));
        TeamResponseDTO mockResponse = new TeamResponseDTO("A", "PDI DEV", user, Arrays.asList(owner));

        when(teamService.removeUser("A", "2")).thenReturn(mockResponse);
        when(teamService.removeUser("B", "2")).thenReturn(null);
        when(teamService.removeUser("A", "3")).thenReturn(null);
        when(teamService.removeUser("A", "1")).thenReturn(null);

        ResponseEntity<TeamResponseDTO> response1 = teamController.removeUserFromTeam("A", "2");
        assertEquals(response1.getBody(), mockResponse);
        assertEquals(response1.getStatusCode(), HttpStatus.OK);

        ResponseEntity<TeamResponseDTO> response2 = teamController.removeUserFromTeam("B", "2");
        assertEquals(response2.getStatusCode(), HttpStatus.BAD_REQUEST);

        ResponseEntity<TeamResponseDTO> response3 = teamController.removeUserFromTeam("A", "3");
        assertEquals(response3.getStatusCode(), HttpStatus.BAD_REQUEST);

        ResponseEntity<TeamResponseDTO> response4 = teamController.removeUserFromTeam("A", "1");
        assertEquals(response4.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void deleteTeamTest(){

        when(teamService.deleteTeam("A")).thenReturn(true);
        when(teamService.deleteTeam("B")).thenReturn(false);

        ResponseEntity responseEntity = teamController.deleteTeam("A");
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);

        ResponseEntity responseEntity2 = teamController.deleteTeam("B");
        assertEquals(responseEntity2.getStatusCode(), HttpStatus.BAD_REQUEST);

    }
}































