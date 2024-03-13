package ru.shulenin.farmownerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Dto баллов для отпарвки сообщения
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreSendDto {
    private Long id;
    private Long workerId;
    private Integer score;
    private LocalDate date;
}
