package ru.shulenin.farmownerapi.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProductivityReportWithDate extends ProductivityReport {
    private LocalDate date;

    public ProductivityReportWithDate(WorkerReadDto worker, ProductReadDto product, Double reportAmount, Double planAmount, LocalDate date) {
        super(worker, product, reportAmount, planAmount);
        this.date = date;
    }
}

