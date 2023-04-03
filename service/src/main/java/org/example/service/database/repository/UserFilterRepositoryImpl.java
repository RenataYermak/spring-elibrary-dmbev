package org.example.service.database.repository;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.AllArgsConstructor;
import org.example.service.database.entity.User;
import org.example.service.database.querydsl.QPredicate;
import org.example.service.dto.UserFilter;

import javax.persistence.EntityManager;
import java.util.List;

import static org.example.service.database.entity.QUser.user;

@AllArgsConstructor
public class UserFilterRepositoryImpl implements UserFilterRepository {

    private final EntityManager entityManager;

    @Override
    public List<User> findByFilter(UserFilter filter) {
        var predicate = QPredicate.builder()
                .add(filter.firstname(), user.firstname::eq)
                .add(filter.lastname(), user.lastname::eq)
                .add(filter.email(), user.email::eq)
                .add(filter.role(), user.role::eq)
                .buildAnd();

        return new JPAQuery<User>(entityManager)
                .select(user)
                .from(user)
                .where(predicate)
                .fetch();
    }
}
