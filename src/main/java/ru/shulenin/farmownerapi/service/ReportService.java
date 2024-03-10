package ru.shulenin.farmownerapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shulenin.farmownerapi.datasource.repository.ProductRepository;
import ru.shulenin.farmownerapi.datasource.repository.ReportRepository;
import ru.shulenin.farmownerapi.datasource.repository.WorkerRepository;
import ru.shulenin.farmownerapi.dto.ReportReceiveDto;
import ru.shulenin.farmownerapi.mapper.ReportMapper;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final WorkerRepository workerRepository;
    private final ProductRepository productRepository;

    private final ReportMapper reportMapper = ReportMapper.INSTANCE;

    @Transactional
    public void save(ReportReceiveDto reportDto) {
        var report = reportMapper.reportReceiveDtoToReport(reportDto, workerRepository, productRepository);
        reportRepository.save(report);
    }
}
