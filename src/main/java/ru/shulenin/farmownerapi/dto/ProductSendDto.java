package ru.shulenin.farmownerapi.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.shulenin.farmownerapi.datasource.entity.Product;

/**
 * Dto продукта для отправки сообщения
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSendDto {
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    private Product.Measure measure;
}
