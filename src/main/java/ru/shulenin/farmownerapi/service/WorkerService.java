package ru.shulenin.farmownerapi.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shulenin.farmownerapi.datasource.entity.Worker;
import ru.shulenin.farmownerapi.datasource.redis.repository.WorkerRedisRepository;
import ru.shulenin.farmownerapi.datasource.repository.WorkerRepository;
import ru.shulenin.farmownerapi.dto.WorkerReadDto;
import ru.shulenin.farmownerapi.dto.WorkerSaveEditDto;
import ru.shulenin.farmownerapi.dto.WorkerSendDto;
import ru.shulenin.farmownerapi.exception.ThereAreNotEntities;
import ru.shulenin.farmownerapi.mapper.WorkerMapper;

import java.util.List;
import java.util.Optional;

/**
 * Сурвис для работы с рабочими
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class WorkerService {
    private final WorkerRepository workerRepository;
    private final KafkaTemplate<Long, WorkerSendDto> kafkaWorkerTemplate;
    private final WorkerRedisRepository workerRedisRepository;
    private final WorkerMapper workerMapper = WorkerMapper.INSTANCE;

    /**
     * Инициализация кэша
     */
    @PostConstruct
    public void init() {
        workerRedisRepository.clear();
    }

    /**
     * Получить всех рабочих
     * @return список рабочих
     * @throws ThereAreNotEntities
     */
    public List<WorkerReadDto> findAll() throws ThereAreNotEntities {
        if (workerRedisRepository.isEmpty()) {
            List<Worker> workers = workerRepository.findAll();
            workerRedisRepository.saveAll(workers);
            log.info("WorkerService.findAll: all entities saved to cash");

            return workers.stream()
                    .map(workerMapper::workerToWorkerReadDto)
                    .toList();
        }

        return workerRedisRepository.findAll()
                .stream()
                .map(workerMapper::workerToWorkerReadDto)
                .toList();
    }

    /**
     * Получить рабочего по id
     * @param id id рабочгео
     * @return dto рабочего для чтения
     */
    public Optional<WorkerReadDto> findById(Long id) {
        var worker = workerRedisRepository.findById(id);

        if (worker.isEmpty()) {
            worker = workerRepository.findById(id);

            worker.map(wrk -> {
                workerRedisRepository.save(wrk);
                log.info(String.format("WorkerService.findById: entity %s saved to cache", wrk));

                return wrk;
            }).orElseGet(() -> {
                log.warn(String.format("WorkerService.findById: entity with id=%d does not exist", id));
                return null;
            });
        }
        return worker
                .map(workerMapper::workerToWorkerReadDto);
    }

    /**
     * Сохранить рабочего
     * @param workerDto dto рабочгео для сохранения
     * @return dto рабочего для чтения
     */
    @Transactional
    public Optional<WorkerReadDto> save(WorkerSaveEditDto workerDto) {
        Worker worker = workerMapper.workerSaveEditdtoToWorker(workerDto);

        workerRepository.saveAndFlush(worker);
        log.info(String.format("WorkerService.save: entity %s saved", worker));

        workerRedisRepository.save(worker);
        log.info(String.format("WorkerService.save: %s saved to cache", worker));

        var message = workerMapper.workerToWorkerSendDto(worker);

        kafkaWorkerTemplate.send("worker.save", message);
        log.info(String.format("WorkerService.save: message %s sent", message));

        return Optional.of(worker)
                .map(workerMapper::workerToWorkerReadDto);
    }

    /**
     * Удалить рабочего по id
     * @param id id рабочего
     */
    @Transactional
    public void delete(Long id) {
        var worker = workerRedisRepository.findById(id);

        worker.map(wrk -> {
            var message = workerMapper.workerToWorkerSendDto(wrk);
            workerRepository.deleteById(id);
            workerRedisRepository.delete(id);

            log.info(String.format("WorkerService.delete: entity %s deleted", wrk));
            log.info(String.format("WorkerService.delete: entity %s deleted from cache", wrk));

            kafkaWorkerTemplate.send("worker.delete", message);
            log.info(String.format("WorkerService.delete: message %s sent", message));

            return wrk;
        }).orElseGet(() -> {
            log.warn(String.format("WorkerService.save: entity with id=%d not found", id));
            return null;
        });
    }
}
