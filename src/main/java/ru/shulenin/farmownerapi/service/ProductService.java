package ru.shulenin.farmownerapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shulenin.farmownerapi.datasource.entity.Product;
import ru.shulenin.farmownerapi.datasource.redis.repository.ProductRedisRepository;
import ru.shulenin.farmownerapi.datasource.repository.ProductRepository;
import ru.shulenin.farmownerapi.dto.ProductReadDto;
import ru.shulenin.farmownerapi.dto.ProductSaveEditDto;
import ru.shulenin.farmownerapi.dto.ProductSendDto;
import ru.shulenin.farmownerapi.mapper.ProductMapper;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с продуктами
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductRedisRepository productRedisRepository;

    private final KafkaTemplate<Long, ProductSendDto> kafkaProductTemplate;

    private final ProductMapper productMapper = ProductMapper.INSTANCE;

    /**
     * Получить все продукты
     * @return список продуктов
     */
    public List<ProductReadDto> findAll() {
        return productRedisRepository.findAll()
                .stream()
                .map(productMapper::productToReadDto)
                .toList();
    }

    /**
     * Получить продукт по id
     * @param id id продукта
     * @return dto продукта для чтения
     */
    public Optional<ProductReadDto> findById(Long id) {
        var product = productRedisRepository.findById(id);

        if (product.isEmpty()) {
            product = productRepository.findById(id);

            product.map(prod -> {
                productRedisRepository.save(prod);

                log.info(String.format("ProductService.findById: entity %s saved to cache", prod));

                return prod;
            }).orElseGet(() -> {
                log.warn(String.format("ProductService.findById: entity with id=%d does not exist", id));
                return null;
            });
        }

        return product
                .map(productMapper::productToReadDto);
    }

    /**
     * Сохранить продукт
     * @param productDto dto продукта для сохранения
     * @return dto продукта для чтения
     */
    @Transactional
    public Optional<ProductReadDto> save(ProductSaveEditDto productDto) {
        Product product = productMapper.productSaveEditDtoToProduct(productDto);

        productRepository.saveAndFlush(product);
        log.info(String.format("ProductService.save: entity: %s saved", product));

        productRedisRepository.save(product);
        log.info(String.format("ProductService.save: entity %s saved to cache", product));

        var message = productMapper.productToSendDto(product);

        kafkaProductTemplate.send("product.save", message);
        log.info(String.format("ProductService.save: message %s sent", message));

        return Optional.of(product)
                .map(productMapper::productToReadDto);
    }

    /**
     * Удалить продукт по id
     * @param id id продукта
     */
    @Transactional
    public void delete(Long id) {
        var product = productRedisRepository.findById(id);

        product.map(prod -> {
            var message = productMapper.productToSendDto(prod);
            productRepository.deleteById(id);
            productRedisRepository.delete(id);

            log.info(String.format("ProductService.delete: entity %s deleted", prod));
            log.info(String.format("ProductService.delete: entity %s deleted from cache", prod));

            kafkaProductTemplate.send("product.delete", message);
            log.info(String.format("ProductService.delete: message %s sent", message));

            return prod;
        }).orElseGet(() -> {
            log.warn(String.format("ProductService.delete: entity with id=%d not found", id));
            return null;
        });
    }
}
