package ru.shulenin.farmownerapi.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.shulenin.farmownerapi.datasource.entity.Product;

/**
 * Dto продукта для сохранения
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSaveEditDto extends AbstractDto {
    @NotBlank(message = "product name is required field and can not be empty")
    private String name;

    @Enumerated(EnumType.STRING)
    private Product.Measure measure;
}
