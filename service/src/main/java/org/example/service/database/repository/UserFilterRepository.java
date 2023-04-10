package org.example.service.database.repository;

import org.example.service.database.entity.User;
import org.example.service.dto.userDto.UserFilter;

import java.util.List;

public interface UserFilterRepository {

    List<User> findAllByFilter(UserFilter filter);
}

