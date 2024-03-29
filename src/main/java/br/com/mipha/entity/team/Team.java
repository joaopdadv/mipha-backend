package br.com.mipha.entity.team;

import br.com.mipha.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "teams")
public class Team {

    @Id
    private String id;
    private String name;
    @DBRef
    private User owner;
    @DBRef
    private List<User> users = new ArrayList<>();

}
