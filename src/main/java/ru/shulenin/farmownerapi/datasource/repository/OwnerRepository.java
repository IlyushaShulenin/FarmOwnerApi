package ru.shulenin.farmownerapi.datasource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shulenin.farmownerapi.datasource.entity.Owner;

public interface OwnerRepository extends JpaRepository<Owner, Integer> {
    public Owner findByEmail(String email);
}
