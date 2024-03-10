package ru.shulenin.farmownerapi.mapper;

import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.shulenin.farmownerapi.datasource.entity.Plan;
import ru.shulenin.farmownerapi.datasource.entity.Score;
import ru.shulenin.farmownerapi.datasource.repository.WorkerRepository;
import ru.shulenin.farmownerapi.dto.ScoreReadDto;
import ru.shulenin.farmownerapi.dto.ScoreSaveEditDto;
import ru.shulenin.farmownerapi.dto.ScoreSendDto;

@Mapper
public interface ScoreMapper {
    ScoreMapper INSTANCE = Mappers.getMapper( ScoreMapper.class );

    default public ScoreReadDto scoreToScoreReadDto(Score score, WorkerMapper workerMapper) {
        var worker = workerMapper.workerToWorkerReadDto(score.getWorker());

        return new ScoreReadDto(
                score.getId(),
                worker,
                score.getScore(),
                score.getDate()
        );
    }

    default public Score scoreSaveEditDtoToScore(ScoreSaveEditDto scoreDto,
                                                 WorkerRepository workerRepository) {
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

    public ScoreSendDto scoreToScoreSendDto(Score score);
}
