package org.example.service.dto.userDto;

import lombok.Builder;

@Builder
public record UserFilter(String email,
                         String firstname,
                         String lastname) {
}
