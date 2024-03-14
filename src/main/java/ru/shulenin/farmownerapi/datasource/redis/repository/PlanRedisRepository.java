package ru.shulenin.farmownerapi.datasource.redis.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.shulenin.farmownerapi.datasource.entity.Plan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Redis репозиторий для работы с планами
 */
@Repository
@RequiredArgsConstructor
public class PlanRedisRepository implements RedisRepository<Plan, Long> {

    private static final String KEY = "PLAN";

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Получить все планы
     * @return список планов
     */
    @Override
    public List<Plan> findAll() {
        return redisTemplate.opsForHash().values(KEY)
                .stream()
                .map(pln -> (Plan) pln)
                .toList();
    }

    /**
     * Получить план по id
     * @param id id плана
     * @return план
     */
    @Override
    public Optional<Plan> findById(Long id) {
        var plan = (Plan) redisTemplate.opsForHash().get(KEY, id);
        return Optional.ofNullable(plan);
    }

    /**
     * Сохранить план
     * @param entity сущность
     */
    @Override
    public void save(Plan entity) {
        redisTemplate.opsForHash().put(KEY, entity.getId(), entity);
    }

    /**
     * Сохранить список планов
     * @param entities список планов
     */
    @Override
    public void saveAll(List<Plan> entities) {
        if (!entities.isEmpty()) {
            Map<Long, Plan> entries = new HashMap<>();

            for (var entity : entities)
                entries.put(entity.getId(), entity);

            redisTemplate.opsForHash().putAll(KEY, entries);
        }
    }

    /**
     * Удаление плана по id
     * @param id id плана
     */
    @Override
    public void delete(Long id) {
        redisTemplate.opsForHash().delete(KEY, id);
    }

    /**
     * Проверка кэшв на пустоту
     * @return true если пусто, иначе false
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
