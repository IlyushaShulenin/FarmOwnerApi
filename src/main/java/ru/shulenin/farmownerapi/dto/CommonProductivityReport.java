package ru.shulenin.farmownerapi.dto;

/**
 * Dto выработки рабочего
 */
public class CommonProductivityReport extends ProductivityReport {
    public CommonProductivityReport(WorkerReadDto worker, ProductReadDto product, Double reportAmount, Double planAmount) {
        super(worker, product, reportAmount, planAmount);
    }
}
