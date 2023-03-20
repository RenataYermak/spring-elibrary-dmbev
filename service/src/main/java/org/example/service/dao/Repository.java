package org.example.service.dao;

import org.example.service.database.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyMap;

public interface Repository<T extends Serializable, E extends BaseEntity<T>> {

    E save(E entity);

    void delete(E entity);

    void update(E entity);

    List<E> findAll();

    default Optional<E> findById(T id) {
        return findById(id, emptyMap());
    }

    Optional<E> findById(T id, Map<String, Object> properties);
}
