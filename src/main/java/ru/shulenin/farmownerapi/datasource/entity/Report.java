package ru.shulenin.farmownerapi.datasource.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Сущность отчета
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report implements Serializable {
    @Serial
    @Transient
    private static final long serialVersionUID = 5L;

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

    public Report(Worker worker, Product product,
                  Float amount, LocalDate date, Boolean planIsCompleted) {
        this.worker = worker;
        this.product = product;
        this.amount = amount;
        this.date = date;
        this.planIsCompleted = planIsCompleted;
    }
}
