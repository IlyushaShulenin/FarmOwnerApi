package ru.shulenin.farmownerapi.datasource.redis.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.shulenin.farmownerapi.datasource.entity.Score;

import java.util.List;
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

    }

    @Override
    public void saveAll(List<Score> entities) throws Exception {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void clear() {

    }
}
