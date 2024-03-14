package ru.shulenin.farmownerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Dto плана для чтения
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanReadDto extends AbstractDto {
    private WorkerReadDto worker;
    private ProductReadDto product;
    private Float amount;
    private LocalDate date;
}
