package ru.shulenin.farmownerapi.datasource.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * Сущность рабочего
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Worker implements Serializable {
    @Serial
    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    private String surname;

    /**
     * Необходимо для увольнения работника,
     * чтобы не удалять сохранять отчеты и планы уволенного сотрудника
     */
    private Boolean isWorking = true;

    public Worker(String email, String password, String name, String surname) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }
}
