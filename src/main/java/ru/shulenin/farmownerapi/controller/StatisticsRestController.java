package ru.shulenin.farmownerapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.shulenin.farmownerapi.dto.ProductReport;
import ru.shulenin.farmownerapi.dto.ProductivityReport;
import ru.shulenin.farmownerapi.service.ReportService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/owner-api/v1/statistics")
public class StatisticsRestController {
    private final ReportService reportService;

    /**
     * Результаты выработки рабочего(данные об объеме проведенной работы в сравнении с планами)
     * @param id id рабочего
     * @param month месяц
     * @return результаты выработки
     */
    @GetMapping("/productivity/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductivityReport> getProductivity(@PathVariable("id") Long id,
                                                    @RequestParam(value = "month", required = false) Integer month) {
        if (month == null)
            return reportService.getProductivityForWorker(id);
        return reportService.getProductivityForWorkerByMonth(id, month);
    }
}
