package ru.shulenin.farmownerapi.datasource.redis.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.shulenin.farmownerapi.TestBase;
import ru.shulenin.farmownerapi.annotation.IntegrationTest;
import ru.shulenin.farmownerapi.datasource.entity.Score;
import ru.shulenin.farmownerapi.datasource.entity.Worker;
import ru.shulenin.farmownerapi.datasource.repository.ScoreRepository;
import ru.shulenin.farmownerapi.datasource.repository.WorkerRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScoreRedisRepositoryTest extends TestBase implements RedisRepositoryTest {
    private final ScoreRedisRepository redisRepository;
    private final ScoreRepository jpaRepository;
    private final WorkerRepository workerRepository;

    @Override
    @BeforeEach
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

        var worker = new Worker(
                "test",
                "test",
                "test"
        );

        workerRepository.saveAndFlush(worker);

        var entity = new Score(
                worker,
                10,
                LocalDate.now()
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
                "test"
        );
        var entity2 = new Worker(
                "test2",
                "test",
                "test"
        );
        workerRepository.saveAndFlush(entity1);
        workerRepository.saveAndFlush(entity2);

        var score1 = new Score(
            entity1,
            10,
            LocalDate.now()
        );
        var score2 = new Score(
            entity2,
            9,
            LocalDate.now()
        );

        List<Score> entities = List.of(score1, score2);

        jpaRepository.saveAll(entities);
        redisRepository.saveAll(entities);

        var allFromJpaRepository = jpaRepository.findAll();
        var allFromRedisRepository = redisRepository.findAll();
        var haveEqualSize = checkSize(allFromJpaRepository, allFromRedisRepository);
        assertThat(haveEqualSize).isTrue();

        var entityFromJpa1 = jpaRepository.findById(21L);
        var entityFromJpa2 = jpaRepository.findById(22L);

        var entityFromRedis1 = redisRepository.findById(21L);
        var entityFromRedis2 = redisRepository.findById(22L);

        assertThat(entityFromJpa1).isEqualTo(entityFromRedis1);
        assertThat(entityFromJpa2).isEqualTo(entityFromRedis2);
    }

    @Test
    @Override
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
    @Override
    public void clearTest() {
        redisRepository.clear();
        assertThat(redisRepository.findAll()).isEqualTo(Collections.emptyList());
    }

    private boolean checkSize(List<Score> workersFromJpa, List<Score> workersFromRedis) {
        var sizeOfaAlFromJpaRepository = workersFromJpa.size();
        var sizeOfAllFromRedisRepository = workersFromRedis.size();

        return sizeOfaAlFromJpaRepository == sizeOfAllFromRedisRepository;
    }
}
