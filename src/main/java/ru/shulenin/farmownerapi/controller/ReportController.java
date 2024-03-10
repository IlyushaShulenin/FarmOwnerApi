package ru.shulenin.farmownerapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.shulenin.farmownerapi.dto.ReportReceiveDto;
import ru.shulenin.farmownerapi.service.ReportService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/owner-api/v1/report")
public class ReportController {
    private final ReportService reportService;

    @PostMapping
    @KafkaListener(id = "ReportSave", topics = {"report.save"}, containerFactory = "singleFactory")
    public void save(ReportReceiveDto reportDto) {
        reportService.save(reportDto);
    }
}
