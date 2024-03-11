package ru.shulenin.farmownerapi.datasource.redis.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.shulenin.farmownerapi.datasource.entity.Report;
import ru.shulenin.farmownerapi.exception.ThereAreNotEntities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReportRedisRepository implements RedisRepository<Report, Long> {
    private static final String KEY = "REPORT";

    private final RedisTemplate<String, Object> reportRedisTemplate;

    @Override
    public List<Report> findAll() {
        return reportRedisTemplate.opsForHash().values(KEY)
                .stream()
                .map(prod -> (Report) prod)
                .toList();
    }

    @Override
    public Optional<Report> findById(Long id) {
        var report = (Report) reportRedisTemplate.opsForHash().get(KEY, id);
        return Optional.ofNullable(report);
    }

    @Override
    public void save(Report entity) {
        reportRedisTemplate.opsForHash().put(KEY, entity.getId(), entity);
    }

    @Override
    public void saveAll(List<Report> entities) throws ThereAreNotEntities {
        if (entities.isEmpty())
            throw new ThereAreNotEntities("There are not workers");

        Map<Long, Report> entries = new HashMap<>();

        for (var entity : entities)
            entries.put(entity.getId(), entity);

        reportRedisTemplate.opsForHash().putAll(KEY, entries);
    }

    @Override
    public void delete(Long id) {
        reportRedisTemplate.opsForHash().delete(KEY, id);
    }

    @Override
    public boolean isEmpty() {
        var values = reportRedisTemplate.opsForHash().values(KEY);
        return values.isEmpty();
    }

    @Override
    public void clear() {
        reportRedisTemplate.delete(KEY);
    }
}
