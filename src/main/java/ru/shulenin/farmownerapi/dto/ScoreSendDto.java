package ru.shulenin.farmownerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreSendDto {
    private Long id;
    private Long workerId;
    private Integer score;
    private LocalDate date;
}
