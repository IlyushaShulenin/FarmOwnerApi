package ru.shulenin.farmownerapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.shulenin.farmownerapi.dto.JwtAuthenticationResponse;
import ru.shulenin.farmownerapi.dto.SignInRequest;
import ru.shulenin.farmownerapi.dto.SignUpRequest;
import ru.shulenin.farmownerapi.service.AuthenticationService;

/**
 * Контроллер для аутентификации
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthRestController {
    private final AuthenticationService authenticationService;

    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    /**
     * Аутентификация владельца
     * @param request dto для аутентификации
     * @return JWT токен
     */
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        return authenticationService.signIn(request);
    }
}
