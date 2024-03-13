package ru.shulenin.farmownerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Dto отчета
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportReadDto {
    private WorkerReadDto worker;
    private ProductReadDto product;
    private Float amount;
    private LocalDate date;
    private Boolean planIsCompleted;
}
