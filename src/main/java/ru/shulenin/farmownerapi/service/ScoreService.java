package ru.shulenin.farmownerapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shulenin.farmownerapi.datasource.redis.repository.ScoreRedisRepository;
import ru.shulenin.farmownerapi.datasource.repository.ScoreRepository;
import ru.shulenin.farmownerapi.dto.ScoreReadDto;
import ru.shulenin.farmownerapi.dto.ScoreSendDto;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScoreService {
    private final ScoreRepository scoreRepository;
    private final KafkaTemplate<Long, ScoreSendDto> kafkaScoreTemplate;
    private final ScoreRedisRepository scoreRedisRepository;

    public List<ScoreReadDto> findAll() {

    }
}
