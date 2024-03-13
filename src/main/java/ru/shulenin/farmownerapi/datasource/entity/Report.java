package ru.shulenin.farmownerapi.datasource.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Сущность отчета
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "worker_id")
    private Worker worker;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Float amount;

    private LocalDate date;

    private Boolean planIsCompleted;
}
