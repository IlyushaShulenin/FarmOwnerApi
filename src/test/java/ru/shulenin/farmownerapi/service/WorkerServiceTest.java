package ru.shulenin.farmownerapi.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import ru.shulenin.farmownerapi.TestBase;
import ru.shulenin.farmownerapi.annotation.IntegrationTest;
import ru.shulenin.farmownerapi.datasource.entity.Worker;
import ru.shulenin.farmownerapi.datasource.repository.WorkerRepository;
import ru.shulenin.farmownerapi.dto.WorkerReadDto;
import ru.shulenin.farmownerapi.dto.WorkerSaveEditDto;
import ru.shulenin.farmownerapi.mapper.WorkerMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@IntegrationTest
@RequiredArgsConstructor
class WorkerServiceTest extends TestBase {
    private final WorkerService service;
    private final WorkerRepository repository;

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
        var dtoFromRepository = workerMapper.workerToWorkerReadDto(entity);

        assertThat(dtoFromService).isEqualTo(dtoFromRepository);

        var wrongId = -1L;
        var dtoBadCaseFromService = service.findById(wrongId);

        assertThat(dtoBadCaseFromService).isEmpty();
    }

    @Test
    void save() {
        var id = 6L;

        var saveDto = new WorkerSaveEditDto(
                "test@mail.com",
                "test",
                "test",
                "test"
        );

        var readDto = service.save(saveDto);

        assertThat(readDto).isPresent();
        assertThat(readDto.get().getEmail()).isEqualTo("test@mail.com");
        assertThat(readDto.get().getName()).isEqualTo("test");
        assertThat(readDto.get().getSurname()).isEqualTo("test");

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

    private boolean checkSize(List<WorkerReadDto> workersFromService,
                              List<Worker> workerFromRepository) {
        var sizeOfaAlFromService = workersFromService.size();
        var sizeOfAllFromRepository = workerFromRepository.size();

        return sizeOfaAlFromService == sizeOfAllFromRepository;
    }
}