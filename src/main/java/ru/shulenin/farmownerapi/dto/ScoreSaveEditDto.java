package ru.shulenin.farmownerapi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreSaveEditDto extends AbstractDto {
    @Positive(message = "workerId must be positive")
    private Long workerId;

    @Positive(message = "score must be positive")
    @Min(value = 1, message = "score must be greater then 0")
    @Max(value = 10, message = "score must be less then 10")
    private Integer score;

    @PastOrPresent(message = "date can not be a future")
    private LocalDate date;
}
