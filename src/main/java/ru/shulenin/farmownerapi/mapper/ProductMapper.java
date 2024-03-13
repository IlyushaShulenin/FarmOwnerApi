package ru.shulenin.farmownerapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.shulenin.farmownerapi.datasource.entity.Product;
import ru.shulenin.farmownerapi.dto.ProductReadDto;
import ru.shulenin.farmownerapi.dto.ProductSaveEditDto;
import ru.shulenin.farmownerapi.dto.ProductSendDto;

/**
 * Маппер для продуктов
 */
@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper( ProductMapper.class );

    /**
     * От сущности к dto для чтения
     * @param product сущность
     * @return dto для чтения
     */
    public ProductReadDto productToReadDto(Product product);

    /**
     * От сущности к сообщению
     * @param product сущность
     * @return сообщение
     */
    public ProductSendDto productToSendDto(Product product);

    /**
     * От dto для сохранения к сущности
     * @param product dto для сохранения
     * @return сущность
     */
    public Product productSaveEditDtoToProduct(ProductSaveEditDto product);
}
