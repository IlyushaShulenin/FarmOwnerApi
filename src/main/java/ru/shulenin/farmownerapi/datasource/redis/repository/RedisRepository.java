package ru.shulenin.farmownerapi.datasource.redis.repository;

import java.util.List;
import java.util.Optional;

public interface RedisRepository<E, K> {
    public List<E> findAll();

    public Optional<E> findById(K id);

    public void save(E entity);

    public void saveAll(List<E> entities);

    public void delete(K id);

    public boolean isEmpty();

    public void clear();

}
