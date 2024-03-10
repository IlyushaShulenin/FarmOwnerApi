package ru.shulenin.farmownerapi.datasource.redis.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.shulenin.farmownerapi.datasource.entity.Plan;
import ru.shulenin.farmownerapi.exception.ThereAreNotEntities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PlanRedisRepository implements RedisRepository<Plan, Long> {

    private static final String KEY = "PLAN";

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<Plan> findAll() {
        return redisTemplate.opsForHash().values(KEY)
                .stream()
                .map(pln -> (Plan) pln)
                .toList();
    }

    @Override
    public Optional<Plan> findById(Long id) {
        var plan = (Plan) redisTemplate.opsForHash().get(KEY, id);
        return Optional.ofNullable(plan);
    }

    @Override
    public void save(Plan entity) {
        redisTemplate.opsForHash().put(KEY, entity.getId(), entity);
    }

    @Override
    public void saveAll(List<Plan> entities) throws ThereAreNotEntities {
        if (entities.isEmpty())
            throw new ThereAreNotEntities("There are not workers");

        Map<Long, Plan> entries = new HashMap<>();

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
