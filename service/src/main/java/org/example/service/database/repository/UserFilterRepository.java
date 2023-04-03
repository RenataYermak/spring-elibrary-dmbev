package org.example.service.database.repository;

import org.example.service.database.entity.User;
import org.example.service.dto.UserFilter;

import java.util.List;

public interface UserFilterRepository {

    List<User> findByFilter(UserFilter filter);
}

