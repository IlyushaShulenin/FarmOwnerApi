package ru.shulenin.farmownerapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.shulenin.farmownerapi.dto.ProductReport;
import ru.shulenin.farmownerapi.service.ReportService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/owner-api/v1/statistics")
public class StatisticsRestController {
    private final ReportService reportService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductReport> findAll() {
        return reportService.getProductReport();
    }
}
