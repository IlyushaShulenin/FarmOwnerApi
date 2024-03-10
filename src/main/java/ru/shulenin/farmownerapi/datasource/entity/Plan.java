package ru.shulenin.farmownerapi.datasource.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Plan implements Serializable {
    @Serial
    @Transient
    private static final long serialVersionUID = 3L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    private Worker worker;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer amount;

    private LocalDate date;

    public Plan(Worker worker, Product product, Integer amount, LocalDate date) {
        this.worker = worker;
        this.product = product;
        this.amount = amount;
        this.date = date;
    }
}
