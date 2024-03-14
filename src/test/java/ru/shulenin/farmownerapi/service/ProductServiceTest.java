package ru.shulenin.farmownerapi.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import ru.shulenin.farmownerapi.TestBase;
import ru.shulenin.farmownerapi.annotation.IntegrationTest;
import ru.shulenin.farmownerapi.datasource.entity.Product;
import ru.shulenin.farmownerapi.datasource.repository.ProductRepository;
import ru.shulenin.farmownerapi.dto.ProductReadDto;
import ru.shulenin.farmownerapi.dto.ProductSaveEditDto;
import ru.shulenin.farmownerapi.mapper.ProductMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.shulenin.farmownerapi.datasource.entity.Product.Measure.UNIT;

@IntegrationTest
@RequiredArgsConstructor
public class ProductServiceTest extends TestBase {
    private final ProductService service;
    private final ProductRepository repository;

    private final ProductMapper productMapper = ProductMapper.INSTANCE;

    @Test
    void findAll() {
        var dataFromService = service.findAll();
        var dataFromRepository = repository.findAll();

        var sizeIsEqual = checkSize(dataFromService, dataFromRepository);

        assertThat(sizeIsEqual).isTrue();
    }

    @Test
    void findById() {
        var id = 1L;

        var dtoFromService = service.findById(id).get();
        var entity = repository.findById(id).get();
        var dtoFromRepository = productMapper.productToReadDto(entity);

        assertThat(dtoFromService).isEqualTo(dtoFromRepository);

        var wrongId = -1L;
        var dtoBadCaseFromService = service.findById(wrongId);

        assertThat(dtoBadCaseFromService).isEmpty();
    }

    @Test
    void save() {
        var id = 7L;

        var saveDto = new ProductSaveEditDto(
                "test",
                UNIT
        );

        var readDto = service.save(saveDto);

        assertThat(readDto).isPresent();
        assertThat(readDto.get().getName()).isEqualTo("test");
        assertThat(readDto.get().getMeasure()).isEqualTo(UNIT);

        var savedEntity = service.findById(id);

        assertThat(savedEntity).isNotEmpty();
    }

    @Test
    void delete() {
        var id = 1L;

        service.delete(id);
        var deletedEntity = service.findById(id);

        assertThat(deletedEntity).isEmpty();
    }

    private boolean checkSize(List<ProductReadDto> workersFromService,
                              List<Product> workerFromRepository) {
        var sizeOfaAlFromService = workersFromService.size();
        var sizeOfAllFromRepository = workerFromRepository.size();

        return sizeOfaAlFromService == sizeOfAllFromRepository;
    }
}
