package org.example.service.mapper.userMapper;

import org.example.service.database.entity.User;
import org.example.service.dto.userDto.UserCreateEditDto;
import org.example.service.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class UserCreateEditMapper implements Mapper<UserCreateEditDto, User> {

    @Override
    public User map(UserCreateEditDto fromObject, User toObject) {
        copy(fromObject, toObject);

        return toObject;
    }

    @Override
    public User map(UserCreateEditDto object) {
        User user = new User();
        copy(object, user);

        return user;
    }

    private void copy(UserCreateEditDto object, User user) {
        user.setFirstname(object.getFirstname());
        user.setLastname(object.getLastname());
        user.setEmail(object.getEmail());
        user.setPassword(object.getPassword());
        user.setRole(object.getRole());
    }
}
