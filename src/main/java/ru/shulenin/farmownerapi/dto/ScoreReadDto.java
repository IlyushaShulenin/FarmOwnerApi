package ru.shulenin.farmownerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreReadDto extends AbstractDto {
    private Long id;
    private WorkerReadDto worker;
    private Integer score;
    private LocalDate date;
}
