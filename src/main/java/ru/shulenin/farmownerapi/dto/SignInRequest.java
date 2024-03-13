package ru.shulenin.farmownerapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {
    @NotBlank(message = "email is required field and can not be empty")
    @Email(message = "email should look like user@somemail.com")
    private String email;

    @NotBlank(message = "password is required field and can not be empty")
    private String password;
}
