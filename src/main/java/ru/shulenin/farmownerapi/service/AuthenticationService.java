package ru.shulenin.farmownerapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shulenin.farmownerapi.datasource.entity.Owner;
import ru.shulenin.farmownerapi.dto.JwtAuthenticationResponse;
import ru.shulenin.farmownerapi.dto.SignInRequest;
import ru.shulenin.farmownerapi.dto.SignUpRequest;

@Service
@RequiredArgsConstructor
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

        var user = new Owner(2,
                request.getEmail(),
                passwordEncoder.encode(request.getPassword())
               );

        ownerService.create(user);

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    /**
     * Аутентификация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));

        var owner = ownerService
                .loadUserByUsername(request.getEmail());

        var jwt = jwtService.generateToken(owner);

        return new JwtAuthenticationResponse(jwt);
    }
}
