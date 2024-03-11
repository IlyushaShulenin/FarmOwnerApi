package ru.shulenin.farmownerapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.shulenin.farmownerapi.datasource.entity.Report;
import ru.shulenin.farmownerapi.datasource.repository.ProductRepository;
import ru.shulenin.farmownerapi.datasource.repository.WorkerRepository;
import ru.shulenin.farmownerapi.dto.PlanReadDto;
import ru.shulenin.farmownerapi.dto.ProductReport;
import ru.shulenin.farmownerapi.dto.ReportReadDto;
import ru.shulenin.farmownerapi.dto.ReportReceiveDto;

@Mapper
public interface ReportMapper {

    ReportMapper INSTANCE = Mappers.getMapper( ReportMapper.class );

    public ProductReport reportToProductReport(Report report);

    default public Report reportReceiveDtoToReport(ReportReceiveDto report, WorkerRepository workerRepository,
                                                   ProductRepository productRepository) {
        var worker = workerRepository.getReferenceById(report.getWorkerId());
        var product = productRepository.getReferenceById(report.getProductId());

        return new Report(
                report.getId(),
                worker,
                product,
                report.getAmount(),
                report.getDate(),
                report.getPlanIsCompleted()
        );
    }

    default public ReportReadDto reportToReportReadDto(Report report,
                                                       WorkerMapper workerMapper,
                                                       ProductMapper productMapper) {
        var workerDto = workerMapper.workerToWorkerReadDto(report.getWorker());
        var productDto = productMapper.productToReadDto(report.getProduct());

        return new ReportReadDto(
                workerDto,
                productDto,
                report.getAmount(),
                report.getDate(),
                report.getPlanIsCompleted()
        );
    }
}
