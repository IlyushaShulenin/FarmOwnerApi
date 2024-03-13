package ru.shulenin.farmownerapi.datasource.redis.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.shulenin.farmownerapi.datasource.entity.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Redis репозиторий для работы с продуктами
 */
@Repository
@RequiredArgsConstructor
public class ProductRedisRepository implements RedisRepository<Product, Long> {
    private static final String KEY = "PRODUCT";

    private final RedisTemplate<String, Object> redisTemplate;


    /**
     * Получить все продукты
     * @return список продуктов
     */
    @Override
    public List<Product> findAll() {
        return redisTemplate.opsForHash().values(KEY)
                .stream()
                .map(prod -> (Product) prod)
                .toList();
    }

    /**
     * Получить продукт по id
     * @param id id продукта
     * @return продукт
     */
    @Override
    public Optional<Product> findById(Long id) {
        var product = (Product) redisTemplate.opsForHash().get(KEY, id);
        return Optional.ofNullable(product);
    }

    /**
     * Сохранить продукт
     * @param entity сущность
     */
    @Override
    public void save(Product entity) {
        redisTemplate.opsForHash().put(KEY, entity.getId(), entity);
    }

    /**
     * Сохранить список продуктов
     * @param entities список продуктов
     */
    @Override
    public void saveAll(List<Product> entities) {
        if (entities.isEmpty()) {
            Map<Long, Product> entries = new HashMap<>();

            for (var entity : entities)
                entries.put(entity.getId(), entity);

            redisTemplate.opsForHash().putAll(KEY, entries);
        }
    }

    /**
     * Удалить продукт по id
     * @param id id продукта
     */
    @Override
    public void delete(Long id) {
        redisTemplate.opsForHash().delete(KEY, id);
    }

    /**
     * Проверка кэша на пустоту
     * @return true еслт пусто, иначе false
     */
    @Override
    public boolean isEmpty() {
        var values = redisTemplate.opsForHash().values(KEY);
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
