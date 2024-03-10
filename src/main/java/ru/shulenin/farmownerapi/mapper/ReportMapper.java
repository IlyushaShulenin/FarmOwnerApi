package ru.shulenin.farmownerapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.shulenin.farmownerapi.datasource.entity.Report;
import ru.shulenin.farmownerapi.datasource.repository.ProductRepository;
import ru.shulenin.farmownerapi.datasource.repository.WorkerRepository;
import ru.shulenin.farmownerapi.dto.ReportReceiveDto;

@Mapper
public interface ReportMapper {

    ReportMapper INSTANCE = Mappers.getMapper( ReportMapper.class );

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
}
