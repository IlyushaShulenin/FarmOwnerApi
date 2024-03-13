package ru.shulenin.farmownerapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shulenin.farmownerapi.datasource.entity.Owner;
import ru.shulenin.farmownerapi.dto.JwtAuthenticationResponse;
import ru.shulenin.farmownerapi.dto.SignInRequest;
import ru.shulenin.farmownerapi.dto.SignUpRequest;

/**
 * Сервис для аутентификации
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final OwnerService ownerService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Регистрация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    @Transactional
    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        var email = request.getEmail();
        var user = new Owner(
                email,
                passwordEncoder.encode(request.getPassword())
               );

        ownerService.create(user);
        log.info(String.format("User %s created", email));

        var jwt = jwtService.generateToken(user);
        log.info(String.format("Token for %s created", email));

        return new JwtAuthenticationResponse(jwt);
    }

    /**
     * Аутентификация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        var email = request.getEmail();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                email,
                request.getPassword()
        ));
        log.info(String.format("User %s authenticated", email));

        var owner = ownerService
                .loadUserByUsername(email);

        var jwt = jwtService.generateToken(owner);
        log.info(String.format("Token for %s created", email));

        return new JwtAuthenticationResponse(jwt);
    }
}
