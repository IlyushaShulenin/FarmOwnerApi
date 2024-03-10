package ru.shulenin.farmownerapi.datasource.redis.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.shulenin.farmownerapi.datasource.entity.Score;
import ru.shulenin.farmownerapi.datasource.entity.Worker;
import ru.shulenin.farmownerapi.exception.ThereAreNotEntities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ScoreRedisRepository implements RedisRepository<Score, Long> {
    private final static String KEY = "SCORE";

    private final RedisTemplate<String, Object> scoreRedisTemplate;

    @Override
    public List<Score> findAll() {
        return scoreRedisTemplate.opsForHash().values(KEY)
                .stream()
                .map(scr -> (Score) scr)
                .toList();
    }

    @Override
    public Optional<Score> findById(Long id) {
        var score = (Score) scoreRedisTemplate.opsForHash().get(KEY, id);
        return Optional.ofNullable(score);
    }

    @Override
    public void save(Score entity) {
        scoreRedisTemplate.opsForHash().put(KEY, entity.getId(), entity);
    }

    @Override
    public void saveAll(List<Score> entities) throws ThereAreNotEntities {
        if (entities.isEmpty())
            throw new ThereAreNotEntities("There are not workers");

        Map<Long, Score> entries = new HashMap<>();

        for (var entity : entities)
            entries.put(entity.getId(), entity);

        scoreRedisTemplate.opsForHash().putAll(KEY, entries);
    }

    @Override
    public void delete(Long id) {
        scoreRedisTemplate.opsForHash().delete(KEY, id);
    }

    @Override
    public boolean isEmpty() {
        var values = scoreRedisTemplate.opsForHash().values(KEY);
        return values.isEmpty();
    }

    @Override
    public void clear() {
        scoreRedisTemplate.delete(KEY);
    }
}
