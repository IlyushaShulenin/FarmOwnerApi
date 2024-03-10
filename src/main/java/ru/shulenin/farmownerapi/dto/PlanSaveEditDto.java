package ru.shulenin.farmownerapi.dto;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.Instant;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanSaveEditDto extends AbstractDto {
    @Positive
    private Long workerId;

    @Positive
    private Long productId;

    @Positive
    private Integer amount;

    @PastOrPresent
    private LocalDate date;
}
