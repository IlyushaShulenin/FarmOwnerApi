package ru.shulenin.farmownerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.shulenin.farmownerapi.datasource.entity.Product;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductReadDto extends AbstractDto {
    private Long id;
    private String name;
    private Product.Measure measure;
}
