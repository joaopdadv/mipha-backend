package br.com.mipha.entity.user;

import br.com.mipha.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {

    private String name;
    private String lastName;
    private String email;
    private String password;
    private UserRole role;
}
