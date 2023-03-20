package org.example.service.dao;

import com.querydsl.jpa.impl.JPAQuery;
import org.example.service.database.entity.User;
import org.example.service.dto.UserFilter;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static org.example.service.database.entity.QUser.user;

@Repository
public class UserRepository extends BaseRepository<Long, User> {

    public UserRepository(EntityManager entityManager) {
        super(User.class, entityManager);
    }

    public List<User> findAllByEmailAndPassword(String email, String password) {
        return new JPAQuery<User>(entityManager)
                .select(user)
                .from(user)
                .where(user.email.eq(email).and(user.password.eq(password)))
                .fetch();
    }

    public List<User> findUsersByQuery(UserFilter filter) {
        var predicate = QPredicate.builder()
                .add(filter.getFirstname(), user.firstname::eq)
                .add(filter.getLastname(), user.lastname::eq)
                .add(filter.getEmail(), user.email::eq)
                .add(filter.getRole(), user.role::eq)
                .buildAnd();

        return new JPAQuery<User>(entityManager)
                .select(user)
                .from(user)
                .where(predicate)
                .fetch();
    }
}