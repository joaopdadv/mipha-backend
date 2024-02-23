package br.com.mipha.entity.team;

import br.com.mipha.entity.user.UserNoTeamsResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamNoUsersResponseDTO {
    private String id;
    private String name;
}
