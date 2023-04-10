package org.example.service.dto.userDto;

import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.example.service.database.entity.Role;

@Value
@FieldNameConstants
public class UserCreateEditDto {
    String firstname;
    String lastname;
    String email;
    String password;
    Role role;
}
