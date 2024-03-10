package ru.shulenin.farmownerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportReceiveDto {
    private Long id;
    private Long workerId;
    private Long productId;
    private Float amount;
    private LocalDate date;
    private Boolean planIsCompleted;
}
