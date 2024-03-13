package ru.shulenin.farmownerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductivityReport {
    private WorkerReadDto worker;
    private ProductReadDto product;
    private Double reportAmount;
    private Double planAmount;
}
