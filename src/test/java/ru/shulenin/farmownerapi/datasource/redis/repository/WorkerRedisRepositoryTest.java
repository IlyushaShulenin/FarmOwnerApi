package ru.shulenin.farmownerapi.datasource.redis.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import ru.shulenin.farmownerapi.TestBase;
import ru.shulenin.farmownerapi.annotation.IntegrationTest;
import ru.shulenin.farmownerapi.datasource.entity.Worker;
import ru.shulenin.farmownerapi.datasource.repository.WorkerRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@RequiredArgsConstructor
@Transactional(readOnly = true)
class WorkerRedisRepositoryTest extends TestBase implements RedisRepositoryTest {
    private final WorkerRedisRepository redisRepository;
    private final WorkerRepository jpaRepository;

    @BeforeEach
    @Override
    public void init() {
        redisRepository.clear();
        redisRepository.saveAll(jpaRepository.findAll());
    }

    @Test
    @Override
    public void findAllTest() {
        var allFromJpaRepository = jpaRepository.findAll();
        var allFromRedisRepository = redisRepository.findAll();
        var haveEqualSize = checkSize(allFromJpaRepository, allFromRedisRepository);
        assertThat(haveEqualSize).isTrue();
    }

    @Test
    @Override
    public void findByIdTest() {
        var entityFromJpa = jpaRepository.findById(1L);
        var entityFromRedis = redisRepository.findById(1L);

        assertThat(entityFromRedis).isEqualTo(entityFromJpa);
    }

    @Test
    @Transactional
    @Override
    public void saveTest() {
        var id = 6L;

        var entity = new Worker(
                "test",
                "test",
                "test",
                "test"
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
    @Override
    public void saveAllTest() {
        var entity1 = new Worker(
                "test1",
                "test",
                "test",
                "test"
        );
        var entity2 = new Worker(
                "test2",
                "test",
                "test",
                "test"
        );

        List<Worker> entities = List.of(entity1, entity2);

        jpaRepository.saveAll(entities);
        redisRepository.saveAll(entities);

        var allFromJpaRepository = jpaRepository.findAll();
        var allFromRedisRepository = redisRepository.findAll();
        var haveEqualSize = checkSize(allFromJpaRepository, allFromRedisRepository);
        assertThat(haveEqualSize).isTrue();

        var entityFromJpa6 = jpaRepository.findById(6L);
        var entityFromJpa67 = jpaRepository.findById(7L);

        var entityFromRedis6 = redisRepository.findById(6L);
        var entityFromRedis7 = redisRepository.findById(7L);

        assertThat(entityFromJpa6).isEqualTo(entityFromRedis6);
        assertThat(entityFromJpa67).isEqualTo(entityFromRedis7);
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

    private boolean checkSize(List<Worker> workersFromJpa, List<Worker> workersFromRedis) {
        var sizeOfaAlFromJpaRepository = workersFromJpa.size();
        var sizeOfAllFromRedisRepository = workersFromRedis.size();

        return sizeOfaAlFromJpaRepository == sizeOfAllFromRedisRepository;
    }
}