package ru.shulenin.farmownerapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shulenin.farmownerapi.datasource.entity.Owner;
import ru.shulenin.farmownerapi.datasource.repository.OwnerRepository;

/**
 * Сервис для работы с владельцем
 */
@Service
@RequiredArgsConstructor
@Transactional
public class OwnerService implements UserDetailsService {
    private final OwnerRepository ownerRepository;

    public Owner save(Owner user) {
        return ownerRepository.save(user);
    }

    /**
     * Создание пользователя
     *
     * @return созданный пользователь
     */
    public Owner create(Owner user) {
        return save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return ownerRepository.findByEmail(email);
    }
}
