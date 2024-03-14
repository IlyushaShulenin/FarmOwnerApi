package ru.shulenin.farmownerapi.controller;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.shulenin.farmownerapi.dto.ProductivityReport;
import ru.shulenin.farmownerapi.service.MailService;
import ru.shulenin.farmownerapi.service.OwnerService;
import ru.shulenin.farmownerapi.service.ReportService;

import java.util.List;

/**
 * Контроллер для получения общей информации о производительности рабочих
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/owner-api/v1/statistics")
public class StatisticsRestController {
    private final static String EMAIL_SUBJECT = "Report";

    private final ReportService reportService;
    private final MailService mailService;
    private final OwnerService ownerService;

    /**
     * Результаты выработки рабочего(данные об объеме проведенной работы в сравнении с планами)
     * @param id id рабочего
     * @param month месяц
     * @return результаты выработки
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductivityReport> getProductivity(@PathVariable("id") Long id,
                                                    @RequestParam(value = "month", required = false) Integer month) {
        if (month == null)
            return reportService.getProductivity(id);
        return reportService.getProductivity(id, month);
    }

    @GetMapping("/send")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductivityReport> sendProductivity() throws MessagingException {
        var commonProductivity = reportService.getProductivity();
        var email = ownerService.getOwner().getEmail();

        mailService.send(email, EMAIL_SUBJECT, commonProductivity.toString());

        return commonProductivity;
    }
}
