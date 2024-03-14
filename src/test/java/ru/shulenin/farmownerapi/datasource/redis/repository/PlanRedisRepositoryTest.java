package ru.shulenin.farmownerapi.datasource.redis.repository;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import ru.shulenin.farmownerapi.TestBase;
import ru.shulenin.farmownerapi.annotation.IntegrationTest;
import ru.shulenin.farmownerapi.datasource.entity.Plan;
import ru.shulenin.farmownerapi.datasource.entity.Product;
import ru.shulenin.farmownerapi.datasource.entity.Worker;
import ru.shulenin.farmownerapi.datasource.repository.PlanRepository;
import ru.shulenin.farmownerapi.datasource.repository.ProductRepository;
import ru.shulenin.farmownerapi.datasource.repository.WorkerRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.shulenin.farmownerapi.datasource.entity.Product.Measure.UNIT;

@IntegrationTest
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanRedisRepositoryTest extends TestBase implements RedisRepositoryTest {
    private final PlanRedisRepository redisRepository;
    private final PlanRepository jpaRepository;
    private final WorkerRepository workerRepository;
    private final ProductRepository productRepository;

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
        var id = 21L;

        var worker = new Worker(
                "test",
                "test",
                "test",
                "test"
        );
        var product = new Product(
                "test",
                UNIT
        );

        workerRepository.saveAndFlush(worker);
        productRepository.saveAndFlush(product);

        var entity = new Plan(
                worker,
                product,
                100F,
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
        var worker1 = new Worker(
                "test1",
                "test",
                "test",
                "test"
        );
        var worker2 = new Worker(
                "test2",
                "test",
                "test",
                "test"
        );

        var product1 = new Product(
             "test1",
             UNIT
        );
        var product2 = new Product(
            "test2",
            UNIT
        );

        workerRepository.saveAllAndFlush(List.of(worker1, worker2));
        productRepository.saveAllAndFlush(List.of(product1, product2));

        var entity1 = new Plan(
            worker1,
            product1,
            100F,
            LocalDate.now()
        ) ;
        var entity2 = new Plan(
            worker2,
            product2,
            200F,
            LocalDate.now()
        );

        List<Plan> entities = List.of(entity1, entity2);

        jpaRepository.saveAll(entities);
        redisRepository.saveAll(entities);

        var allFromJpaRepository = jpaRepository.findAll();
        var allFromRedisRepository = redisRepository.findAll();
        var haveEqualSize = checkSize(allFromJpaRepository, allFromRedisRepository);
        assertThat(haveEqualSize).isTrue();

        var entityFromJpa6 = jpaRepository.findById(21L);
        var entityFromJpa67 = jpaRepository.findById(22L);

        var entityFromRedis6 = redisRepository.findById(21L);
        var entityFromRedis7 = redisRepository.findById(22L);

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

    private boolean checkSize(List<Plan> workersFromJpa, List<Plan> workersFromRedis) {
        var sizeOfaAlFromJpaRepository = workersFromJpa.size();
        var sizeOfAllFromRedisRepository = workersFromRedis.size();

        return sizeOfaAlFromJpaRepository == sizeOfAllFromRedisRepository;
    }
}
