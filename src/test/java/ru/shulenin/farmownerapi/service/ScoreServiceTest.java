package ru.shulenin.farmownerapi.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import ru.shulenin.farmownerapi.TestBase;
import ru.shulenin.farmownerapi.annotation.IntegrationTest;
import ru.shulenin.farmownerapi.datasource.entity.Score;
import ru.shulenin.farmownerapi.datasource.repository.ScoreRepository;
import ru.shulenin.farmownerapi.dto.ScoreReadDto;
import ru.shulenin.farmownerapi.dto.ScoreSaveEditDto;
import ru.shulenin.farmownerapi.mapper.ScoreMapper;
import ru.shulenin.farmownerapi.mapper.WorkerMapper;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@RequiredArgsConstructor
public class ScoreServiceTest extends TestBase {
    private final ScoreService service;
    private final ScoreRepository repository;

    private final ScoreMapper scoreMapper = ScoreMapper.INSTANCE;
    private final WorkerMapper workerMapper = WorkerMapper.INSTANCE;

    @Test
    void findAll() {
        var dataFromService = service.findAll();
        var dataFromRepository = repository.findAll();

        var sizeIsEqual = checkSize(dataFromService, dataFromRepository);

        assertThat(sizeIsEqual).isTrue();
    }

    @Test
    void findById() {
        var id = 1L;

        var dtoFromService = service.findById(id).get();
        var entity = repository.findById(id).get();
        var dtoFromRepository = scoreMapper.scoreToScoreReadDto(entity, workerMapper);

        assertThat(dtoFromService).isEqualTo(dtoFromRepository);

        var wrongId = -1L;
        var dtoBadCaseFromService = service.findById(wrongId);

        assertThat(dtoBadCaseFromService).isEmpty();
    }

    @Test
    void save() {
        var id = 21L;

        var saveDto = new ScoreSaveEditDto(
                1L,
                10,
                LocalDate.now()
        );

        var readDto = service.save(saveDto);

        assertThat(readDto).isPresent();
        assertThat(readDto.get().getWorker().getId()).isEqualTo(1L);
        assertThat(readDto.get().getScore()).isEqualTo(10);
        assertThat(readDto.get().getDate()).isEqualTo(LocalDate.now());

        var savedEntity = service.findById(id);

        assertThat(savedEntity).isNotEmpty();
    }

    @Test
    void delete() {
        var id = 1L;

        service.delete(id);
        var deletedEntity = service.findById(id);

        assertThat(deletedEntity).isEmpty();
    }

    private boolean checkSize(List<ScoreReadDto> workersFromService,
                              List<Score> workerFromRepository) {
        var sizeOfaAlFromService = workersFromService.size();
        var sizeOfAllFromRepository = workerFromRepository.size();

        return sizeOfaAlFromService == sizeOfAllFromRepository;
    }
}
