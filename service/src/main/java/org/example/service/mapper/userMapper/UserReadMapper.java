package org.example.service.mapper.userMapper;

import lombok.RequiredArgsConstructor;
import org.example.service.database.entity.User;
import org.example.service.dto.userDto.UserReadDto;
import org.example.service.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReadMapper implements Mapper<User, UserReadDto> {

    @Override
    public UserReadDto map(User object) {
        return new UserReadDto(
                object.getId(),
                object.getFirstname(),
                object.getLastname(),
                object.getEmail(),
                object.getRole()
        );
    }
}
