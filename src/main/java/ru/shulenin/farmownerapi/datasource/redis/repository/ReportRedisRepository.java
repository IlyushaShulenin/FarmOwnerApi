package ru.shulenin.farmownerapi.datasource.redis.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.shulenin.farmownerapi.datasource.entity.Report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Redis репозиторий для работы с отчетами
 */
@Repository
@RequiredArgsConstructor
public class ReportRedisRepository implements RedisRepository<Report, Long> {
    private static final String KEY = "REPORT";

    private final RedisTemplate<String, Object> reportRedisTemplate;

    /**
     * Получить все отчеты
     * @return список отчетов
     */
    @Override
    public List<Report> findAll() {
        return reportRedisTemplate.opsForHash().values(KEY)
                .stream()
                .map(prod -> (Report) prod)
                .toList();
    }

    /**
     * Получить отчет по id
     * @param id id отчета
     * @return отчет
     */
    @Override
    public Optional<Report> findById(Long id) {
        var report = (Report) reportRedisTemplate.opsForHash().get(KEY, id);
        return Optional.ofNullable(report);
    }

    /**
     * Сохранить сущность
     * @param entity сущность
     */
    @Override
    public void save(Report entity) {
        reportRedisTemplate.opsForHash().put(KEY, entity.getId(), entity);
    }

    /**
     * Сохранить список сущностей
     * @param entities спписок сущностей
     */
    @Override
    public void saveAll(List<Report> entities) {
        if (entities.isEmpty()) {
            Map<Long, Report> entries = new HashMap<>();

            for (var entity : entities)
                entries.put(entity.getId(), entity);

            reportRedisTemplate.opsForHash().putAll(KEY, entries);
        }
    }

    /**
     * Удаление отчета по id
     * @param id id отчета
     */
    @Override
    public void delete(Long id) {
        reportRedisTemplate.opsForHash().delete(KEY, id);
    }

    /**
     * Проверка кэша на пустоту
     * @return true если пусто, иначе false
     */
    @Override
    public boolean isEmpty() {
        var values = reportRedisTemplate.opsForHash().values(KEY);
        return values.isEmpty();
    }

    /**
     * Очистка кэша
     */
    @Override
    public void clear() {
        reportRedisTemplate.delete(KEY);
    }
}
