package ru.shulenin.farmownerapi.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shulenin.farmownerapi.datasource.entity.Report;
import ru.shulenin.farmownerapi.datasource.redis.repository.ReportRedisRepository;
import ru.shulenin.farmownerapi.datasource.repository.ProductRepository;
import ru.shulenin.farmownerapi.datasource.repository.ReportRepository;
import ru.shulenin.farmownerapi.datasource.repository.WorkerRepository;
import ru.shulenin.farmownerapi.dto.ProductReport;
import ru.shulenin.farmownerapi.dto.ReportReadDto;
import ru.shulenin.farmownerapi.dto.ReportReceiveDto;
import ru.shulenin.farmownerapi.exception.ThereAreNotEntities;
import ru.shulenin.farmownerapi.mapper.ProductMapper;
import ru.shulenin.farmownerapi.mapper.ReportMapper;
import ru.shulenin.farmownerapi.mapper.WorkerMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReportService {
    private final ReportRepository reportRepository;
    private final WorkerRepository workerRepository;
    private final ProductRepository productRepository;
    private final ReportRedisRepository reportRedisRepository;

    private final EntityManagerFactory entityManagerFactory;

    private final ReportMapper reportMapper = ReportMapper.INSTANCE;
    private final WorkerMapper workerMapper = WorkerMapper.INSTANCE;
    private final ProductMapper productMapper = ProductMapper.INSTANCE;

    public List<ReportReadDto> findAll() throws ThereAreNotEntities {
        if (reportRedisRepository.isEmpty()) {
            List<Report> reports = reportRepository.findAll();
            reportRedisRepository.saveAll(reports);

            log.info("ReportService.findAll(): all entities fetch to cash");

            return reports.stream()
                    .map(this::toDto)
                    .toList();
        }

        return reportRedisRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public Optional<ReportReadDto> findById(Long id) {
        var report = reportRedisRepository.findById(id);

        if (report.isEmpty()) {
            report = reportRepository.findById(id);

            report.map(rpt -> {
                reportRedisRepository.save(rpt);
                log.info(String.format("ReportService.findById(): Plan(%s) fetched to cache", rpt));

                return rpt;
            }).orElseThrow(() -> {
                log.warn(String.format("ReportService.findById(): Plan(id=%d) does not exist", id));
                return new EntityNotFoundException();
            });
        }

        return report
                .map(this::toDto);
    }

    public List getProductReport() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        var queryResult = entityManager.createNativeQuery(
                "SELECT r.product_id, SUM(r.amount), q.s " +
                "FROM report AS r JOIN (SELECT p.product_ID, SUM(p.amount) AS s FROM plan AS p GROUP BY P.product_id) AS q " +
                "ON r.product_id = q.product_id " +
                "GROUP BY r.product_id, q.s;")
                .getResultList();

        return queryResult;
    }

    public List GetProductivity() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        var queryResult = entityManager.createNativeQuery(
                "SELECT r.worker_id, r.product_id, SUM(r.amount), q.s " +
                "FROM report AS r JOIN (SELECT p.product_ID, SUM(p.amount) AS s FROM plan AS p " +
                "GROUP BY P.product_id) AS q " +
                "ON r.product_id = q.product_id " +
                "GROUP BY r.worker_id,r.product_id, q.s;")
                .getResultList();

        return queryResult;
    }

    @Transactional
    public void save(ReportReceiveDto reportDto) {
        var report = toEntity(reportDto);
        reportRepository.save(report);
    }

    private ReportReadDto toDto(Report report) {
        return reportMapper.reportToReportReadDto(report, workerMapper, productMapper);
    }

    private Report toEntity(ReportReceiveDto reportDto) {
        return reportMapper.reportReceiveDtoToReport(reportDto, workerRepository, productRepository);
    }
}
