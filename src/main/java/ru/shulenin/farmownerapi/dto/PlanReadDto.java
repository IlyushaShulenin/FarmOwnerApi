package ru.shulenin.farmownerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanReadDto extends AbstractDto {
    private WorkerReadDto worker;
    private ProductReadDto product;
    private Integer amount;
    private LocalDate date;
}
