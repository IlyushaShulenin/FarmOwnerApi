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
    @NotBlank(message = "email is required field and can not be empty")
    @Email(message = "email should look like user@somemail.com")
    private String email;

    @NotBlank(message = "name is required field and can not be empty")
    private String name;

    @NotBlank(message = "surname is required filed and can not be empty")
    private String surname;
}
