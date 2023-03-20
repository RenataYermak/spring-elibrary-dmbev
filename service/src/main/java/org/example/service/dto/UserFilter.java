package org.example.service.dto;

import lombok.Builder;
import lombok.Value;
import org.example.service.database.entity.Role;

@Value
@Builder
public class UserFilter {

    String email;
    Role role;
    String firstname;
    String lastname;
}
