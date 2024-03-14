package ru.shulenin.farmownerapi.mapper;

import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.shulenin.farmownerapi.datasource.entity.Score;
import ru.shulenin.farmownerapi.datasource.redis.repository.WorkerRedisRepository;
import ru.shulenin.farmownerapi.dto.ScoreReadDto;
import ru.shulenin.farmownerapi.dto.ScoreSaveEditDto;
import ru.shulenin.farmownerapi.dto.ScoreSendDto;

/**
 * Маппер для баллов
 */
@Mapper
public interface ScoreMapper {
    ScoreMapper INSTANCE = Mappers.getMapper( ScoreMapper.class );

    /**
     * От сущности к dto для чтения
     * @param score сущность
     * @param workerMapper маппер для рабочих
     * @return dto для чтения
     */
    default public ScoreReadDto scoreToScoreReadDto(Score score, WorkerMapper workerMapper) {
        var worker = workerMapper.workerToWorkerReadDto(score.getWorker());

        return new ScoreReadDto(
                score.getId(),
                worker,
                score.getScore(),
                score.getDate()
        );
    }

    /**
     * От dto для сохранения к сущности
     * @param scoreDto  dto для сохранения
     * @param workerRepository репозиторий для рабочих
     * @return сущность
     */
    default public Score scoreSaveEditDtoToScore(ScoreSaveEditDto scoreDto,
                                                 WorkerRedisRepository workerRepository) {
        var worker = workerRepository.findById(scoreDto.getWorkerId());
        var score = new Score();

        worker.map(wrk -> {
            score.setWorker(wrk);
            return wrk;
        }).orElseThrow(EntityNotFoundException::new);

        score.setScore(scoreDto.getScore());
        score.setDate(scoreDto.getDate());

        return score;
    }

    /**
     * От сущности к сообщению
     * @param score сущность
     * @return сообщение
     */
    @Mapping(target = "workerId", source = "score.worker.id")
    public ScoreSendDto scoreToScoreSendDto(Score score);
}
