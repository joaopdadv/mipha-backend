package br.com.mipha.entity.team;

import br.com.mipha.entity.user.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamResponseDTO {
    private String id;
    private String name;
    private UserResponseDTO owner;
    private List<UserResponseDTO> users = new ArrayList<>();
}
