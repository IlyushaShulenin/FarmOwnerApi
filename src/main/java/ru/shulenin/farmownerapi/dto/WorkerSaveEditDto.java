package ru.shulenin.farmownerapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerSaveEditDto extends AbstractDto {
    @Email
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;
}
