package org.example.service.database.repository;

import org.example.service.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, UserFilterRepository, QuerydslPredicateExecutor<User> {

    List<User> findAllByEmailAndPassword(String email, String password);
}
