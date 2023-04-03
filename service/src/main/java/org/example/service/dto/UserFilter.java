package org.example.service.dto;

import lombok.Builder;
import org.example.service.database.entity.Role;

@Builder
public record UserFilter(String email,
                         Role role,
                         String firstname,
                         String lastname) {
}
