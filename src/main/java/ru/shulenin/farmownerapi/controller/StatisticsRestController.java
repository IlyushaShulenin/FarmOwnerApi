package ru.shulenin.farmownerapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.shulenin.farmownerapi.dto.ProductivityReport;
import ru.shulenin.farmownerapi.service.MailService;
import ru.shulenin.farmownerapi.service.ReportService;

import java.util.List;

/**
 * Контроллер для получения общей информации о производительности рабочих
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/owner-api/v1/statistics")
public class StatisticsRestController {
    private final ReportService reportService;
    private final MailService mailService;

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

    @GetMapping("/productivity/send/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductivityReport> getProductivity(@PathVariable("id") Long id,
                                                    @RequestParam(value = "month", required = false) Integer month,
                                                    ModelAndView model) {
        var productivityReport = reportService.getProductivityForWorker(id);
        model.addObject("reports", productivityReport);
        model.setViewName("report-mail");

        if (month == null)
            return reportService.getProductivityForWorker(id);
        return reportService.getProductivityForWorkerByMonth(id, month);
    }
}
