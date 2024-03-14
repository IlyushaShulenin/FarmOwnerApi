package ru.shulenin.farmownerapi.datasource.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * Сущность продукта
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {
    @Serial
    @Transient
    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Measure measure;

    private Boolean isProduced = true;

    public enum Measure {
        LITER, KG, UNIT
    }

    public Product(String name, Measure measure) {
        this.name = name;
        this.measure = measure;
    }
}
