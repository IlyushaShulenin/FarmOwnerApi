package ru.shulenin.farmownerapi.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.shulenin.farmownerapi.datasource.entity.Product;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSaveEditDto extends AbstractDto {
    @NotBlank
    private String name;

    @Enumerated(EnumType.STRING)
    @NotBlank
    private Product.Measure measure;
}
