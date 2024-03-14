package ru.shulenin.farmownerapi.datasource.redis.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import ru.shulenin.farmownerapi.TestBase;
import ru.shulenin.farmownerapi.annotation.IntegrationTest;
import ru.shulenin.farmownerapi.datasource.entity.Product;
import ru.shulenin.farmownerapi.datasource.repository.ProductRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.shulenin.farmownerapi.datasource.entity.Product.Measure.UNIT;

@IntegrationTest
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductRedisRepositoryTest extends TestBase implements RedisRepositoryTest  {
    private final ProductRedisRepository redisRepository;
    private final ProductRepository jpaRepository;

    @BeforeEach
    public void init() {
        redisRepository.clear();
        redisRepository.saveAll(jpaRepository.findAll());
    }

    @Test
    public void findAllTest() {
        var allFromJpaRepository = jpaRepository.findAll();
        var allFromRedisRepository = redisRepository.findAll();
        var haveEqualSize = checkSize(allFromJpaRepository, allFromRedisRepository);
        assertThat(haveEqualSize).isTrue();
    }

    @Test
    public void findByIdTest() {
        var entityFromJpa = jpaRepository.findById(1L);
        var entityFromRedis = redisRepository.findById(1L);

        assertThat(entityFromRedis).isEqualTo(entityFromJpa);
    }

    @Test
    @Transactional
    public void saveTest() {
        var id = 7L;

        var entity = new Product(
                "test",
                UNIT
        );

        jpaRepository.saveAndFlush(entity);
        redisRepository.save(entity);

        var entityFromJpa = jpaRepository.findById(id);
        var entityFromRedis = redisRepository.findById(id);

        assertThat(entityFromRedis).isEqualTo(entityFromJpa);

        var allFromJpaRepository = jpaRepository.findAll();
        var allFromRedisRepository = redisRepository.findAll();
        var haveEqualSize = checkSize(allFromJpaRepository, allFromRedisRepository);
        assertThat(haveEqualSize).isTrue();
    }

    @Test
    @Transactional
    public void saveAllTest() {
        var entity1 = new Product(
                "test1",
                UNIT
        );
        var entity2 = new Product(
                "test2",
                UNIT
        );

        List<Product> entities = List.of(entity1, entity2);

        jpaRepository.saveAll(entities);
        redisRepository.saveAll(entities);

        var allFromJpaRepository = jpaRepository.findAll();
        var allFromRedisRepository = redisRepository.findAll();
        var haveEqualSize = checkSize(allFromJpaRepository, allFromRedisRepository);
        assertThat(haveEqualSize).isTrue();

        var entityFromJpa7 = jpaRepository.findById(7L);
        var entityFromJpa8 = jpaRepository.findById(8L);

        var entityFromRedis7= redisRepository.findById(7L);
        var entityFromRedis8 = redisRepository.findById(8L);

        assertThat(entityFromJpa7).isEqualTo(entityFromRedis7);
        assertThat(entityFromJpa8).isEqualTo(entityFromRedis8);
    }

    @Test
    @Transactional
    public void deleteTest() {
        var id = 1L;

        var originSize = redisRepository.findAll().size();
        redisRepository.delete(id);
        var changedSize = redisRepository.findAll().size();

        assertThat(originSize - 1).isEqualTo(changedSize);

        var deletedFromRedis = redisRepository.findById(id);

        assertThat(deletedFromRedis).isEqualTo(Optional.empty());
    }

    @Test
    public void clearTest() {
        redisRepository.clear();
        assertThat(redisRepository.findAll()).isEqualTo(Collections.emptyList());
    }

    private boolean checkSize(List<Product> workersFromJpa, List<Product> workersFromRedis) {
        var sizeOfaAlFromJpaRepository = workersFromJpa.size();
        var sizeOfAllFromRedisRepository = workersFromRedis.size();

        return sizeOfaAlFromJpaRepository == sizeOfAllFromRedisRepository;
    }
}
