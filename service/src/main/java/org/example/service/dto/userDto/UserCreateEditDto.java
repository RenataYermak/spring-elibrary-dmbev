package org.example.service.dto.userDto;

import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.example.service.database.entity.Role;
import org.example.service.validation.group.CreateAction;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
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

    @NotBlank(groups = CreateAction.class)
    @Size(min = 8, max = 49)
    String rawPassword;

    Role role;
}
