package ru.shulenin.farmownerapi.service;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shulenin.farmownerapi.datasource.entity.Score;
import ru.shulenin.farmownerapi.datasource.redis.repository.ScoreRedisRepository;
import ru.shulenin.farmownerapi.datasource.redis.repository.WorkerRedisRepository;
import ru.shulenin.farmownerapi.datasource.repository.ScoreRepository;
import ru.shulenin.farmownerapi.dto.ScoreReadDto;
import ru.shulenin.farmownerapi.dto.ScoreSaveEditDto;
import ru.shulenin.farmownerapi.dto.ScoreSendDto;
import ru.shulenin.farmownerapi.mapper.ScoreMapper;
import ru.shulenin.farmownerapi.mapper.WorkerMapper;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с баллами
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ScoreService {
    private final ScoreRepository scoreRepository;
    private final ScoreRedisRepository scoreRedisRepository;
    private final WorkerRedisRepository workerRepository;

    private final KafkaTemplate<Long, ScoreSendDto> kafkaScoreTemplate;

    private final ScoreMapper scoreMapper = ScoreMapper.INSTANCE;
    private final WorkerMapper workerMapper = WorkerMapper.INSTANCE;

    /**
     * Инициализация кэша
     */
    @PostConstruct
    public void init() {
        scoreRedisRepository.clear();
        scoreRedisRepository.saveAll(scoreRepository.findAll());
        log.info("ScoreService.init: all entities saved to cash");
    }

    /**
     * Получить все баллы
     * @return список баллов
     */
    public List<ScoreReadDto> findAll() {
        return scoreRedisRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    /**
     * Получить баллы по id
     * @param id id быллов
     * @return dto баллов для чтения
     */
    public Optional<ScoreReadDto> findById(Long id) {
        try {
            var score = scoreRedisRepository.findById(id);

            if (score.isEmpty()) {
                score = scoreRepository.findById(id);

                score.map(scr -> {
                    scoreRedisRepository.save(scr);
                    log.info(String.format("ScoreService.findById: entity %s saved to cache", scr));

                    return scr;
                }).orElseGet(() -> {
                    log.warn(String.format("ScoreService.findById: entity with id=%d does not exist", id));
                    return null;
                });
            }

            return score
                    .map(this::toDto);
        }
        catch (EntityNotFoundException e) {
            log.warn("PlanService.save: entity not found");
            return Optional.empty();
        }
    }

    /**
     * Сохранить баллы
     * @param scoreDto dto баллов для сохранения
     * @return dto баллов для чтения
     */
    @Transactional
    public Optional<ScoreReadDto> save(ScoreSaveEditDto scoreDto) {
        try {
            Score score = toEntity(scoreDto);

            scoreRepository.saveAndFlush(score);
            log.info(String.format("WorkerService.save: entity %s saved", score));

            scoreRedisRepository.save(score);
            log.info(String.format("ScoreService.save: %s saved to cache", score));

            var message = scoreMapper.scoreToScoreSendDto(score);

            kafkaScoreTemplate.send("score.save", message);
            log.info(String.format("ScoreService.save: message %s sent", message));

            return Optional.of(score)
                    .map(this::toDto);
        }
        catch (EntityNotFoundException e) {
            log.warn("ScoreService.save: entity not found");
            return Optional.empty();
        }
    }

    /**
     * Удаление баллов по id
     * @param id id баллов
     */
    @Transactional
    public boolean delete(Long id) {
        var score = scoreRedisRepository.findById(id);

        return score.map(scr -> {
            var message = scoreMapper.scoreToScoreSendDto(scr);

            scoreRepository.deleteById(id);
            scoreRedisRepository.delete(id);

            log.info(String.format("ScoreService.delete: entity %s deleted", scr));
            log.info(String.format("ScoreService.delete: entity %s deleted from cache", scr));

            kafkaScoreTemplate.send("score.delete", message);
            log.info(String.format("send: message(%s) sent", message));

            return true;
        }).orElseGet(() -> {
            log.warn(String.format("ScoreService.delete: entity id=%d not found", id));
            return false;
        });
    }

    /**
     * Маппинг сущности на dto
     * @param score сущность
     * @return dto
     */
    private ScoreReadDto toDto(Score score) {
        return scoreMapper.scoreToScoreReadDto(score, workerMapper);
    }

    /**
     * Маппинг dto на сущность
     * @param scoreDto dto для сохранения
     * @return сущность
     */
    private Score toEntity(ScoreSaveEditDto scoreDto) {
        return scoreMapper.scoreSaveEditDtoToScore(scoreDto, workerRepository);
    }
}
