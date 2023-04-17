package org.example.service.dto.userDto;

import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.example.service.database.entity.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Value
@FieldNameConstants
public class UserCreateEditDto {

    @NotEmpty
    @Size(min = 2, max = 25)
    String firstname;

    @NotEmpty
    @Size(min = 2, max = 25)
    String lastname;

    @Email
    String email;

    @NotEmpty
    @Size(min = 8, max = 49)
    String password;

    Role role;
}
