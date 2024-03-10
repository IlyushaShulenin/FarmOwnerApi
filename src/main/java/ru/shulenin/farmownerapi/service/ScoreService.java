package ru.shulenin.farmownerapi.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shulenin.farmownerapi.datasource.entity.Score;
import ru.shulenin.farmownerapi.datasource.entity.Worker;
import ru.shulenin.farmownerapi.datasource.redis.repository.ScoreRedisRepository;
import ru.shulenin.farmownerapi.datasource.repository.ScoreRepository;
import ru.shulenin.farmownerapi.datasource.repository.WorkerRepository;
import ru.shulenin.farmownerapi.dto.*;
import ru.shulenin.farmownerapi.exception.ThereAreNotEntities;
import ru.shulenin.farmownerapi.mapper.ScoreMapper;
import ru.shulenin.farmownerapi.mapper.WorkerMapper;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ScoreService {
    private final ScoreRepository scoreRepository;
    private final KafkaTemplate<Long, ScoreSendDto> kafkaScoreTemplate;
    private final ScoreRedisRepository scoreRedisRepository;
    private final WorkerRepository workerRepository;

    private final ScoreMapper scoreMapper = ScoreMapper.INSTANCE;
    private final WorkerMapper workerMapper = WorkerMapper.INSTANCE;

    public List<ScoreReadDto> findAll() throws ThereAreNotEntities {
        if (scoreRedisRepository.isEmpty()) {
            List<Score> scores = scoreRepository.findAll();
            scoreRedisRepository.saveAll(scores);

            log.info("ScoreService.findAll(): all entities fetch to cash");

            return scores.stream()
                    .map(this::toDto)
                    .toList();
        }

        return scoreRedisRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public Optional<ScoreReadDto> findById(Long id) {
        var score = scoreRedisRepository.findById(id);

        if (score.isEmpty()) {
            score = scoreRepository.findById(id);

            score.map(scr -> {
                scoreRedisRepository.save(scr);
                log.info(String.format("ScoreService.findById(): Score(%s) fetched to cache", scr));

                return scr;
            }).orElseThrow(() -> {
                log.warn(String.format("WorkerService.findById(): worker(id=%d) does not exist", id));
                return new EntityNotFoundException();
            });
        }

        return score
                .map(this::toDto);
    }

    @Transactional
    public Optional<ScoreReadDto> save(ScoreSaveEditDto scoreDto) {
        Score score = toEntity(scoreDto);

        scoreRepository.saveAndFlush(score);
        log.info(String.format("Save entity: %s", score));

        scoreRedisRepository.save(score);
        log.info(String.format("ScoreService.save(): %s saved to cache", score));

        var message = scoreMapper.scoreToScoreSendDto(score);

        kafkaScoreTemplate.send("score.save", message);
        log.info(String.format("Message sent %s", message));

        return Optional.of(score)
                .map(this::toDto);
    }

    @Transactional
    public void delete(Long id) {
        var score = scoreRedisRepository.findById(id);

        score.map(scr -> {
            var message = scoreMapper.scoreToScoreSendDto(scr);

            scoreRepository.deleteById(id);
            scoreRedisRepository.delete(id);

            log.info(String.format("ScoreService.delete(): entity(%s) deleted", scr));
            log.info(String.format("ScoreService.delete(): entity(%s) deleted from cache", scr));

            kafkaScoreTemplate.send("score.delete", message);
            log.info(String.format("send: message(%s) sent", message));

            return scr;
        }).orElseThrow(() -> {
            log.warn(String.format("delete: entity(id=%d) not found", id));
            return new EntityNotFoundException();
        });
    }

    private ScoreReadDto toDto(Score score) {
        return scoreMapper.scoreToScoreReadDto(score, workerMapper);
    }

    private Score toEntity(ScoreSaveEditDto scoreDto) {
        return scoreMapper.scoreSaveEditDtoToScore(scoreDto, workerRepository);
    }
}
