package org.example.service.dao;

import org.example.service.database.entity.Category;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class CategoryRepository extends BaseRepository<Integer, Category> {

    public CategoryRepository(EntityManager entityManager) {
        super(Category.class, entityManager);
    }
}
