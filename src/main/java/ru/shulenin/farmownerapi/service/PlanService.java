package ru.shulenin.farmownerapi.service;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shulenin.farmownerapi.datasource.entity.Plan;
import ru.shulenin.farmownerapi.datasource.redis.repository.PlanRedisRepository;
import ru.shulenin.farmownerapi.datasource.redis.repository.ProductRedisRepository;
import ru.shulenin.farmownerapi.datasource.redis.repository.WorkerRedisRepository;
import ru.shulenin.farmownerapi.datasource.repository.PlanRepository;
import ru.shulenin.farmownerapi.dto.PlanReadDto;
import ru.shulenin.farmownerapi.dto.PlanSaveEditDto;
import ru.shulenin.farmownerapi.dto.PlanSendDto;
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
    private final WorkerRedisRepository workerRepository;
    private final ProductRedisRepository productRepository;
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
        planRedisRepository.saveAll(planRepository.findAll());
        log.info("WorkerService.init: all entities saved to cash");
    }

    /**
     * Получить все планы
     * @return список планов
     */
    public List<PlanReadDto> findAll() {
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
        try {
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
        } catch (EntityNotFoundException e) {
            log.warn("PlanService.save: entity not found");
            return Optional.empty();
        }
    }

    /**
     * Удаление плана по id
     * @param id id плана
     */
    @Transactional
    public boolean delete(Long id) {
        var plan = planRedisRepository.findById(id);

        return plan.map(pln -> {
            var message = planMapper.planToPlanSendDto(pln);
            planRepository.deleteById(id);
            planRedisRepository.delete(id);

            log.info(String.format("PlanService.delete: entity %s deleted", pln));
            log.info(String.format("PlanService.delete: entity %s deleted from cache", pln));

            kafkaPlanTemplate.send("plan.delete", message);
            log.info(String.format("send: message(%s) sent", message));

            return true;
        }).orElseGet(() -> {
            log.warn(String.format("PlanService.delete: entity id=%d not found", id));
            return false;
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
