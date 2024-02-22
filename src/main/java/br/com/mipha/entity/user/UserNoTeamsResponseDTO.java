package br.com.mipha.entity.user;

import br.com.mipha.entity.team.Team;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserNoTeamsResponseDTO {
    private String id;
    private String name;
    private String lastName;
    private String email;
}
