package br.com.mipha.entity.user;

import br.com.mipha.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private String id;
    private String name;
    private String email;
    private List<Team> teams;
}
