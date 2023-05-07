package org.example.service.database.repository;

import org.example.service.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserFilterRepository, QuerydslPredicateExecutor<User> {

    Optional<User> findByEmail(String email);

   // @Query(value = "SELECT u.id FROM users u WHERE u.email = :email", nativeQuery = true)
    //Optional<Long> findByEmail(String email);
}
