package org.example.service.dao;

import org.example.service.database.entity.Author;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class AuthorRepository extends BaseRepository<Long, Author> {

    public AuthorRepository(EntityManager entityManager) {
        super(Author.class, entityManager);
    }
}
