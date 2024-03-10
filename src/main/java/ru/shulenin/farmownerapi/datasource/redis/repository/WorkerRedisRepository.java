package ru.shulenin.farmownerapi.datasource.redis.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.shulenin.farmownerapi.datasource.entity.Worker;
import ru.shulenin.farmownerapi.exception.ThereAreNotEntities;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class WorkerRedisRepository implements RedisRepository<Worker, Long> {
    private static final String KEY = "WORKER";

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<Worker> findAll() {
        return redisTemplate.opsForHash().values(KEY)
                .stream()
                .map(wrk -> (Worker) wrk)
                .toList();
    }

    @Override
    public Optional<Worker> findById(Long id) {
        var worker = (Worker) redisTemplate.opsForHash().get(KEY, id);
        return Optional.ofNullable(worker);
    }

    @Override
    public void save(Worker entity) {
        redisTemplate.opsForHash().put(KEY, entity.getId(), entity);
    }

    @Override
    public void saveAll(List<Worker> entities) throws ThereAreNotEntities {
        if (entities.isEmpty())
            throw new ThereAreNotEntities("There are not workers");

        Map<Long, Worker> entries = new HashMap<>();

        for (var entity : entities)
            entries.put(entity.getId(), entity);

        redisTemplate.opsForHash().putAll(KEY, entries);
    }

    @Override
    public void delete(Long id) {
        redisTemplate.opsForHash().delete(KEY, id);
    }

    @Override
    public boolean isEmpty() {
        var values= redisTemplate.opsForHash().values(KEY);
        return values.isEmpty();
    }

    @Override
    public void clear() {
        redisTemplate.delete(KEY);
    }
}
