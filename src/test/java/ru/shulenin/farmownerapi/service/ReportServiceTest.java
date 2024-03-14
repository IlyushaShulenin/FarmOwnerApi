package ru.shulenin.farmownerapi.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import ru.shulenin.farmownerapi.TestBase;
import ru.shulenin.farmownerapi.annotation.IntegrationTest;
import ru.shulenin.farmownerapi.datasource.entity.Report;
import ru.shulenin.farmownerapi.datasource.repository.ReportRepository;
import ru.shulenin.farmownerapi.dto.ReportReadDto;
import ru.shulenin.farmownerapi.mapper.ProductMapper;
import ru.shulenin.farmownerapi.mapper.ReportMapper;
import ru.shulenin.farmownerapi.mapper.WorkerMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@RequiredArgsConstructor
public class ReportServiceTest extends TestBase  {
    private final ReportService service;
    private final ReportRepository repository;

    private final ReportMapper reportMapper = ReportMapper.INSTANCE;
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
        var dtoFromRepository = reportMapper.reportToReportReadDto(entity, workerMapper,
                productMapper);

        assertThat(dtoFromService).isEqualTo(dtoFromRepository);

        var wrongId = -1L;
        var dtoBadCaseFromService = service.findById(wrongId);

        assertThat(dtoBadCaseFromService).isEmpty();
    }

    @Test
    public void getProductivityForWorker() {
        var workerId = 4L;

        var productivity = service.getProductivity(workerId);
        assertThat(productivity).hasSize(3);

        var productivityFiltered = productivity.stream()
                .filter(prd -> prd.getProduct().getId() == 5L).toList();

        assertThat(productivityFiltered.get(0).getPlanAmount()).isEqualTo(280);
        assertThat(productivityFiltered.get(0).getReportAmount()).isEqualTo(258);
    }

    @Test
    public void getProductivityForWorkerByMonth() {
        var workerId = 1L;

        var productivity = service.getProductivity(workerId, 10);
        assertThat(productivity).hasSize(3);
    }

    private boolean checkSize(List<ReportReadDto> workersFromService,
                              List<Report> workerFromRepository) {
        var sizeOfaAlFromService = workersFromService.size();
        var sizeOfAllFromRepository = workerFromRepository.size();

        return sizeOfaAlFromService == sizeOfAllFromRepository;
    }
}
