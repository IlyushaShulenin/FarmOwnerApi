package ru.shulenin.farmownerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreSaveEditDto extends AbstractDto {
    private Long workerId;
    private Integer score;
    private Instant date;
}
