package ru.shulenin.farmownerapi.service;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shulenin.farmownerapi.datasource.entity.Report;
import ru.shulenin.farmownerapi.datasource.redis.repository.ProductRedisRepository;
import ru.shulenin.farmownerapi.datasource.redis.repository.ReportRedisRepository;
import ru.shulenin.farmownerapi.datasource.redis.repository.WorkerRedisRepository;
import ru.shulenin.farmownerapi.datasource.repository.ReportRepository;
import ru.shulenin.farmownerapi.dto.*;
import ru.shulenin.farmownerapi.mapper.ProductMapper;
import ru.shulenin.farmownerapi.mapper.ReportMapper;
import ru.shulenin.farmownerapi.mapper.WorkerMapper;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с отчетами
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReportService {
    private final ReportRepository reportRepository;
    private final WorkerRedisRepository workerRepository;
    private final ProductRedisRepository productRepository;
    private final ReportRedisRepository reportRedisRepository;

    private final EntityManagerFactory entityManagerFactory;

    private final ReportMapper reportMapper = ReportMapper.INSTANCE;
    private final WorkerMapper workerMapper = WorkerMapper.INSTANCE;
    private final ProductMapper productMapper = ProductMapper.INSTANCE;

    /**
     * Инициализация кэша
     */
    @PostConstruct
    public void init() {
        reportRedisRepository.clear();
        reportRedisRepository.saveAll(reportRepository.findAll());
        log.info("ScoreService.init: all entities saved to cash");
    }


    /**
     * Получить все отчеты
     * @return список отчетов
     */
    public List<ReportReadDto> findAll() {
        return reportRedisRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    /**
     * Получить отчет по id
     * @param id id отчета
     * @return dto отчета для чтения
     */
    public Optional<ReportReadDto> findById(Long id) {
        var report = reportRedisRepository.findById(id);

        if (report.isEmpty()) {
            report = reportRepository.findById(id);

            report.map(rpt -> {
                reportRedisRepository.save(rpt);
                log.info(String.format("ReportService.findById: entity %s saved to cache", rpt));

                return rpt;
            }).orElseThrow(() -> {
                log.warn(String.format("ReportService.findById: entity id=%d does not exist", id));
                return new EntityNotFoundException();
            });
        }

        return report
                .map(this::toDto);
    }

    /**
     * получение информации о продуктивности рабочего
     * @param workerId id рабочего
     * @return продуктивность рабочего
     */
    public List<ProductivityReport> getProductivity(Long workerId) {
        if (!workerRepository.findById(workerId).isPresent()) {
            log.warn(String.format("PersonalInfoService.getProductivityForWorker: entity with id=% does not exist",
                    workerId));
            return Collections.emptyList();
        }

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        var queryResult = entityManager.createNativeQuery(
                        "SELECT t.worker_id, t.product_id, SUM(t.r_amount), SUM(t.p_amount) FROM " +
                                "(SELECT r.worker_id, r.product_id, r.amount AS r_amount, p.amount AS p_amount " +
                                "FROM report AS r JOIN plan AS p " +
                                "ON r.worker_id = p.worker_id AND r.product_id = p.product_id AND p.date = r.date) AS t " +
                                "GROUP BY t.worker_id, t.product_id " +
                                "HAVING t.worker_id = :id")
                .setParameter("id", workerId)
                .getResultList();

        return downcastToReport(queryResult);
    }

    /**
     * получение информации о продуктивности рабочего по месяцам
     * @param workerId id рабочего
     * @param month месяц
     * @return продуктивность рабочего
     */
    public List<ProductivityReport> getProductivity(Long workerId, Integer month) {
        if (!workerRepository.findById(workerId).isPresent()) {
            log.warn(String.format("PersonalInfoService.getProductivityForWorker: entity with id=% does not exist",
                    workerId));
            return Collections.emptyList();
        }

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        var queryResult = entityManager.createNativeQuery(
                        "SELECT t.worker_id, t.product_id, SUM(t.r_amount), SUM(t.p_amount), t.r_date FROM " +
                                "(SELECT r.worker_id, r.product_id, r.date AS r_date, r.amount AS r_amount, p.amount AS p_amount " +
                                "FROM report AS r JOIN plan AS p " +
                                "ON r.worker_id = p.worker_id AND r.product_id = p.product_id AND p.date = r.date) AS t " +
                                "GROUP BY t.worker_id, t.product_id, t.r_date " +
                                "HAVING t.worker_id = :id AND EXTRACT(MONTH from t.r_date) = :month")
                .setParameter("id", workerId)
                .setParameter("month", month)
                .getResultList();

        return downcastToReport(queryResult);
    }

    public List<ProductivityReport> getProductivity() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        var queryResult = entityManager.createNativeQuery(
                        "SELECT t.worker_id, t.product_id, SUM(t.r_amount), SUM(t.p_amount), t.r_date FROM " +
                                "(SELECT r.worker_id, r.product_id, r.date AS r_date, r.amount AS r_amount, p.amount AS p_amount " +
                                "FROM report AS r JOIN plan AS p " +
                                "ON r.worker_id = p.worker_id AND r.product_id = p.product_id AND p.date = r.date) AS t " +
                                "GROUP BY t.worker_id, t.product_id, t.r_date ")
                .getResultList();

        return downcastToReport(queryResult);
    }

    /**
     * Привести Object к ProductivityReportWithDate или CommonProductivityReport
     * @param queryResult
     * @return
     */
    private List<ProductivityReport> downcastToReport(List queryResult) {
        List<ProductivityReport> productivity = new ArrayList<>();

        for (var row : queryResult) {
            var obj = (Object[]) row;

            var worker = workerRepository.findById((Long) obj[0]).get();
            var product = productRepository.findById((Long) obj[1]).get();
            var reportAmount = (Double) obj[2];
            var planAmount = (Double) obj[3];

            if (obj.length == 5) {
                var date = (Date) obj[4];

                productivity.add(new ProductivityReportWithDate(
                        workerMapper.workerToWorkerReadDto(worker),
                        productMapper.productToReadDto(product),
                        reportAmount,
                        planAmount,
                        date.toLocalDate()
                ));
            }
            else {
                productivity.add(new CommonProductivityReport(
                        workerMapper.workerToWorkerReadDto(worker),
                        productMapper.productToReadDto(product),
                        reportAmount,
                        planAmount
                ));
            }
        }

        return productivity;
    }

    /**
     * Сохранить отчет
     * @param reportDto dto для получения сообщения
     * @return dto отчета для чтения
     */
    @Transactional
    public Optional<ReportReadDto> save(ReportReceiveDto reportDto) {
        var report = toEntity(reportDto);

        reportRepository.saveAndFlush(report);
        log.info(String.format("ReportService.save: entity: %s saved", report));

        reportRedisRepository.save(report);
        log.info(String.format("ReportService.save: entity %s saved to cache", report));


        return Optional.of(report)
                .map(this::toDto);
    }

    /**
     * Маппинг сущности на dto для чтения
     * @param report сущность
     * @return dto для чтения
     */
    private ReportReadDto toDto(Report report) {
        return reportMapper.reportToReportReadDto(report, workerMapper, productMapper);
    }

    /**
     * Маппинг dto на сущность
     * @param reportDto dto
     * @return сущность
     */
    private Report toEntity(ReportReceiveDto reportDto) {
        return reportMapper.reportReceiveDtoToReport(reportDto, workerRepository, productRepository);
    }
}
