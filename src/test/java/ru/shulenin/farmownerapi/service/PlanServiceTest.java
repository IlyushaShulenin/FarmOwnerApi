package ru.shulenin.farmownerapi.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import ru.shulenin.farmownerapi.TestBase;
import ru.shulenin.farmownerapi.annotation.IntegrationTest;
import ru.shulenin.farmownerapi.datasource.entity.Plan;
import ru.shulenin.farmownerapi.datasource.repository.PlanRepository;
import ru.shulenin.farmownerapi.dto.PlanReadDto;
import ru.shulenin.farmownerapi.dto.PlanSaveEditDto;
import ru.shulenin.farmownerapi.mapper.PlanMapper;
import ru.shulenin.farmownerapi.mapper.ProductMapper;
import ru.shulenin.farmownerapi.mapper.WorkerMapper;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@RequiredArgsConstructor
public class PlanServiceTest extends TestBase {
    private final PlanService service;
    private final PlanRepository repository;

    private final PlanMapper planMapper = PlanMapper.INSTANCE;
    private final WorkerMapper workerMapper = WorkerMapper.INSTANCE;
    private final ProductMapper productMapper = ProductMapper.INSTANCE;

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
        var dtoFromRepository = planMapper.planToPlanReadDto(entity, workerMapper,
                productMapper);

        assertThat(dtoFromService).isEqualTo(dtoFromRepository);

        var wrongId = -1L;
        var dtoBadCaseFromService = service.findById(wrongId);

        assertThat(dtoBadCaseFromService).isEmpty();
    }

    @Test
    void save() {
        var id = 21L;

        var saveDto = new PlanSaveEditDto(
                1L,
                1L,
                100F,
                LocalDate.now()
        );

        var readDto = service.save(saveDto);

        assertThat(readDto).isPresent();
        assertThat(readDto.get().getWorker().getId()).isEqualTo(1L);
        assertThat(readDto.get().getProduct().getId()).isEqualTo(1L);
        assertThat(readDto.get().getAmount()).isEqualTo(100);
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

    private boolean checkSize(List<PlanReadDto> workersFromService,
                              List<Plan> workerFromRepository) {
        var sizeOfaAlFromService = workersFromService.size();
        var sizeOfAllFromRepository = workerFromRepository.size();

        return sizeOfaAlFromService == sizeOfAllFromRepository;
    }
}
