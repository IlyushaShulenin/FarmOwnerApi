package ru.shulenin.farmownerapi.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shulenin.farmownerapi.datasource.entity.Plan;
import ru.shulenin.farmownerapi.datasource.redis.repository.PlanRedisRepository;
import ru.shulenin.farmownerapi.datasource.repository.PlanRepository;
import ru.shulenin.farmownerapi.datasource.repository.ProductRepository;
import ru.shulenin.farmownerapi.datasource.repository.WorkerRepository;
import ru.shulenin.farmownerapi.dto.PlanReadDto;
import ru.shulenin.farmownerapi.dto.PlanSaveEditDto;
import ru.shulenin.farmownerapi.dto.PlanSendDto;
import ru.shulenin.farmownerapi.exception.ThereAreNotEntities;
import ru.shulenin.farmownerapi.mapper.PlanMapper;
import ru.shulenin.farmownerapi.mapper.ProductMapper;
import ru.shulenin.farmownerapi.mapper.WorkerMapper;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с планами
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PlanService {
    private final PlanRepository planRepository;
    private final WorkerRepository workerRepository;
    private final ProductRepository productRepository;
    private final PlanRedisRepository planRedisRepository;

    private final KafkaTemplate<Long, PlanSendDto> kafkaPlanTemplate;

    private final PlanMapper planMapper = PlanMapper.INSTANCE;
    private final WorkerMapper workerMapper = WorkerMapper.INSTANCE;
    private final ProductMapper productMapper = ProductMapper.INSTANCE;

    /**
     * Инициализация кэша
     */
    @PostConstruct
    public void init() {
        planRedisRepository.clear();
    }

    /**
     * Получить все планы
     * @return список планов
     * @throws ThereAreNotEntities
     */
    public List<PlanReadDto> findAll() throws ThereAreNotEntities {
        if (planRedisRepository.isEmpty()) {
            List<Plan> plans = planRepository.findAll();
            planRedisRepository.saveAll(plans);

            log.info("PlanService.findAll: all entities saved to cash");

            return plans.stream()
                    .map(this::toDto)
                    .toList();
        }

        return planRedisRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    /**
     * Получить план по id
     * @param id id плана
     * @return план
     */
    public Optional<PlanReadDto> findById(Long id) {
        var plan = planRedisRepository.findById(id);

        if (plan.isEmpty()) {
            plan = planRepository.findById(id);

            plan.map(pln -> {
                planRedisRepository.save(pln);
                log.info(String.format("PlanService.findById: entity %s saved to cache", pln));

                return pln;
            }).orElseGet(() -> {
                log.warn(String.format("PlanService.findById: entity with id=%d does not exist", id));
                return null;
            });
        }

        return plan
                .map(this::toDto);
    }

    /**
     * Сохранить план
     * @param planDto dto плана для сохранения
     * @return dto плана для чтения
     */
    @Transactional
    public Optional<PlanReadDto> save(PlanSaveEditDto planDto) {
        Plan plan = toEntity(planDto);

        planRepository.saveAndFlush(plan);
        log.info(String.format("PlanService.save: entity %s saved", plan));

        planRedisRepository.save(plan);
        log.info(String.format("PlanService.save: entity %s saved to cache", plan));

        var message = planMapper.planToPlanSendDto(plan);

        kafkaPlanTemplate.send("plan.save", message);
        log.info(String.format("PlanService.save: message %s sent", message));

        return Optional.of(plan)
                .map(this::toDto);
    }

    /**
     * Удаление плана по id
     * @param id id плана
     */
    @Transactional
    public void delete(Long id) {
        var plan = planRedisRepository.findById(id);

        plan.map(pln -> {
            var message = planMapper.planToPlanSendDto(pln);
            planRepository.deleteById(id);
            planRedisRepository.delete(id);

            log.info(String.format("PlanService.delete: entity %s deleted", pln));
            log.info(String.format("PlanService.delete: entity %s deleted from cache", pln));

            kafkaPlanTemplate.send("plan.delete", message);
            log.info(String.format("send: message(%s) sent", message));

            return pln;
        }).orElseGet(() -> {
            log.warn(String.format("PlanService.delete: entity id=%d not found", id));
            return null;
        });
    }

    /**
     * Маппинг сущности на dto
     * @param plan сущность
     * @return dto для чтения
     */
    private PlanReadDto toDto(Plan plan) {
        return planMapper.planToPlanReadDto(plan, workerMapper, productMapper);
    }

    /**
     * Маппинг dto на сущность
     * @param plan dto для чтения
     * @return сущность
     */
    private Plan toEntity(PlanSaveEditDto plan) {
        return planMapper.planSaveEditDtoToPlan(plan, workerRepository, productRepository);
    }
}
