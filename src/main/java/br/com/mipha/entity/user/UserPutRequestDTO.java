package br.com.mipha.entity.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPutRequestDTO {
    private String name;
    private String lastName;
}
