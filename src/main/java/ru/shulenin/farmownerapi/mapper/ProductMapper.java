package ru.shulenin.farmownerapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.shulenin.farmownerapi.datasource.entity.Product;
import ru.shulenin.farmownerapi.dto.ProductReadDto;
import ru.shulenin.farmownerapi.dto.ProductSaveEditDto;
import ru.shulenin.farmownerapi.dto.ProductSendDto;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper( ProductMapper.class );

    public ProductReadDto productToReadDto(Product product);
    public ProductSendDto productToSendDto(Product product);
    public Product productSaveEditDtoToProduct(ProductSaveEditDto product);
}
