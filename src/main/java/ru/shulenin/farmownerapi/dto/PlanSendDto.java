package ru.shulenin.farmownerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Dto плана для отправки сообщения
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanSendDto extends AbstractDto {
    private Long id;
    private Long workerId;
    private Long productId;
    private Float amount;
    private LocalDate date;
}
