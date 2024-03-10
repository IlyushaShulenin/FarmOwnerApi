package ru.shulenin.farmownerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreReadDto extends AbstractDto {
    private Long id;
    private WorkerReadDto worker;
    private Integer score;
    private Boolean planIsCompleted;
    private Instant date;
}
