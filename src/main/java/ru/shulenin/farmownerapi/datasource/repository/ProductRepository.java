package ru.shulenin.farmownerapi.datasource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shulenin.farmownerapi.datasource.entity.Product;

import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
