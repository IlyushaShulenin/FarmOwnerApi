package ru.shulenin.farmownerapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto для аутентификации
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "email is required field and can not be empty")
    @Email(message = "email should look like user@somemail.com")
    private String email;

    @NotBlank(message = "password is required field and can not be empty")
    private String password;
}
