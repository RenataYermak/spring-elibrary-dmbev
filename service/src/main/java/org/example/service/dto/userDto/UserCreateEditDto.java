package org.example.service.dto.userDto;

import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.example.service.database.entity.Role;
import org.example.service.validation.annotation.Unique;
import org.example.service.validation.group.CreateAction;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Value
@FieldNameConstants
public class UserCreateEditDto {

    @NotEmpty(message = "First Name must be not empty")
    @Size(min = 2, max = 25, message = "First Name must contains from 2 to 25 symbols")
    String firstname;

    @NotEmpty(message = "Last Name must be not empty")
    @Size(min = 2, max = 25, message = "Last Name must contains from 2 to 25 symbols")
    String lastname;

    @Email
    @Unique(groups = CreateAction.class, message = "This email already exist", fieldName = "email", entityName = "User")
    String email;

    @NotBlank(groups = CreateAction.class, message = "Password must be not empty")
    @Size(min = 8, max = 49, message = "Passport must contains at least 8 symbols ")
    String rawPassword;

    Role role;
}
