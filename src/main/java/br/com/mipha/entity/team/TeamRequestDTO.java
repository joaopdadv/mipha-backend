package br.com.mipha.entity.team;

import br.com.mipha.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamRequestDTO {
    private String name;
    private String ownerId;
}
