package ru.shulenin.farmownerapi.datasource.redis.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.shulenin.farmownerapi.datasource.entity.Score;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Redis рупозиторий для работы с баллами
 */
@Repository
@RequiredArgsConstructor
public class ScoreRedisRepository implements RedisRepository<Score, Long> {
    private final static String KEY = "SCORE";

    private final RedisTemplate<String, Object> scoreRedisTemplate;

    /**
     * Получить все баллы
     * @return список баллов
     */
    @Override
    public List<Score> findAll() {
        return scoreRedisTemplate.opsForHash().values(KEY)
                .stream()
                .map(scr -> (Score) scr)
                .toList();
    }

    /**
     * Получить баллы по id
     * @param id id баллов
     * @return баллы
     */
    @Override
    public Optional<Score> findById(Long id) {
        var score = (Score) scoreRedisTemplate.opsForHash().get(KEY, id);
        return Optional.ofNullable(score);
    }

    /**
     * Сохранить баллы
     * @param entity баллы
     */
    @Override
    public void save(Score entity) {
        scoreRedisTemplate.opsForHash().put(KEY, entity.getId(), entity);
    }

    /**
     * Сохранить список баллов
     * @param entities список баллов
     */
    @Override
    public void saveAll(List<Score> entities) {
        if (!entities.isEmpty()) {
            Map<Long, Score> entries = new HashMap<>();

            for (var entity : entities)
                entries.put(entity.getId(), entity);

            scoreRedisTemplate.opsForHash().putAll(KEY, entries);
        }
    }

    /**
     * Удаление баллов по id
     * @param id id баллов
     */
    @Override
    public void delete(Long id) {
        scoreRedisTemplate.opsForHash().delete(KEY, id);
    }

    /**
     * Проверка кжша на пустоту
     * @return true если путо, иначе false
     */
    @Override
    public boolean isEmpty() {
        var values = scoreRedisTemplate.opsForHash().values(KEY);
        return values.isEmpty();
    }

    /**
     * Очистка кэша
     */
    @Override
    public void clear() {
        scoreRedisTemplate.delete(KEY);
    }
}
