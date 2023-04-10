package org.example.service.dto.userDto;

import lombok.Value;
import org.example.service.database.entity.Role;

@Value
public class UserReadDto {
    Long id;
    String firstname;
    String lastname;
    String email;
    Role role;
}
