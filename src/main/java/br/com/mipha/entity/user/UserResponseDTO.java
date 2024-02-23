package br.com.mipha.entity.user;

import br.com.mipha.entity.team.Team;
import br.com.mipha.entity.team.TeamNoUsersResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private String id;
    private String name;
    private String lastName;
    private String email;
    private List<TeamNoUsersResponseDTO> teams;
}
