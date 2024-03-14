package ru.shulenin.farmownerapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Отчет о месячной продуктивности рабочего
 */
@Getter
@Setter
public class ProductivityReportWithDate extends ProductivityReport {
    private LocalDate date;

    public ProductivityReportWithDate(WorkerReadDto worker, ProductReadDto product, Double reportAmount, Double planAmount, LocalDate date) {
        super(worker, product, reportAmount, planAmount);
        this.date = date;
    }

    @Override
    public String toString() {
        return "ProductivityReportWithDate{" +
                "date=" + date +
                " worker=" + getWorker() +
                " product=" + getProduct() +
                " reportAmount=" + getReportAmount() +
                " planAmount=" + getPlanAmount() +
                '}';
    }
}

