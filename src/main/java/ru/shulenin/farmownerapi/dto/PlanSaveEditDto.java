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
    @Positive(message = "workerId must be positive")
    private Long workerId;

    @Positive(message = "productId must be positive")
    private Long productId;

    @Positive(message = "amount must be positive")
    private Integer amount;

    @PastOrPresent(message ="date can not be a future")
    private LocalDate date;
}
