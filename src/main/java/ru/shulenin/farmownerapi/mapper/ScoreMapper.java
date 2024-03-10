package ru.shulenin.farmownerapi.mapper;

import org.mapstruct.Mapper;
import ru.shulenin.farmownerapi.datasource.entity.Score;
import ru.shulenin.farmownerapi.datasource.repository.WorkerRepository;
import ru.shulenin.farmownerapi.dto.ScoreReadDto;
import ru.shulenin.farmownerapi.dto.ScoreSaveEditDto;

@Mapper
public interface ScoreMapper {

    default public ScoreReadDto scoreToScoreReadDto(Score score, WorkerMapper workerMapper) {
        var worker = workerMapper.workerToWorkerReadDto(score.getWorker());

        return new ScoreReadDto(
                score.getId(),
                worker,
                score.getScore(),
                score.getPlanIsCompleted(),
                score.getDate()
        );
    }


}
