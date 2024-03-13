package ru.shulenin.farmownerapi.datasource.redis.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.shulenin.farmownerapi.datasource.entity.Worker;
import ru.shulenin.farmownerapi.exception.ThereAreNotEntities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Redis репозиторий для работы с рабочими
 */
@Repository
@RequiredArgsConstructor
public class WorkerRedisRepository implements RedisRepository<Worker, Long> {
    private static final String KEY = "WORKER";

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Получить список рабоих
     * @return список рабочих
     */
    @Override
    public List<Worker> findAll() {
        return redisTemplate.opsForHash().values(KEY)
                .stream()
                .map(wrk -> (Worker) wrk)
                .toList();
    }

    /**
     * Получить рабочего по id
     * @param id id рабочего
     * @return рабочий
     */
    @Override
    public Optional<Worker> findById(Long id) {
        var worker = (Worker) redisTemplate.opsForHash().get(KEY, id);
        return Optional.ofNullable(worker);
    }

    /**
     * Сохранить рабочего
     * @param entity рабочий
     */
    @Override
    public void save(Worker entity) {
        redisTemplate.opsForHash().put(KEY, entity.getId(), entity);
    }

    /**
     * Сохранить список рабочих
     * @param entities список рабочих
     * @throws ThereAreNotEntities
     */
    @Override
    public void saveAll(List<Worker> entities) throws ThereAreNotEntities {
        if (entities.isEmpty())
            throw new ThereAreNotEntities("There are not workers");

        Map<Long, Worker> entries = new HashMap<>();

        for (var entity : entities)
            entries.put(entity.getId(), entity);

        redisTemplate.opsForHash().putAll(KEY, entries);
    }

    /**
     * Удаление рабочего по id
     * @param id id рабочего
     */
    @Override
    public void delete(Long id) {
        redisTemplate.opsForHash().delete(KEY, id);
    }

    /**
     * Проверка кэша на пустоту
     * @return true есои пусто, иначе false
     */
    @Override
    public boolean isEmpty() {
        var values= redisTemplate.opsForHash().values(KEY);
        return values.isEmpty();
    }

    /**
     * Очистка кэша
     */
    @Override
    public void clear() {
        redisTemplate.delete(KEY);
    }
}
