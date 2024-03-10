package ru.shulenin.farmownerapi.datasource.redis.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.shulenin.farmownerapi.datasource.entity.Product;
import ru.shulenin.farmownerapi.exception.ThereAreNotEntities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRedisRepository implements RedisRepository<Product, Long> {
    private static final String KEY = "PRODUCT";

    private final RedisTemplate<String, Object> redisTemplate;


    @Override
    public List<Product> findAll() {
        return redisTemplate.opsForHash().values(KEY)
                .stream()
                .map(prod -> (Product) prod)
                .toList();
    }

    @Override
    public Optional<Product> findById(Long id) {
        var product = (Product) redisTemplate.opsForHash().get(KEY, id);
        return Optional.ofNullable(product);
    }

    @Override
    public void save(Product entity) {
        redisTemplate.opsForHash().put(KEY, entity.getId(), entity);
    }

    @Override
    public void saveAll(List<Product> entities) throws ThereAreNotEntities {
        if (entities.isEmpty())
            throw new ThereAreNotEntities("There are not products");

        Map<Long, Product> entries = new HashMap<>();

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
        var values = redisTemplate.opsForHash().values(KEY);
        return values.isEmpty();
    }

    @Override
    public void clear() {
        redisTemplate.delete(KEY);
    }
}
