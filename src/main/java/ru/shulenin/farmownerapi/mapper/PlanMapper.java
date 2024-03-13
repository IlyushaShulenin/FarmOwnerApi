package ru.shulenin.farmownerapi.mapper;

import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.transaction.annotation.Transactional;
import ru.shulenin.farmownerapi.datasource.entity.Plan;
import ru.shulenin.farmownerapi.datasource.redis.repository.ProductRedisRepository;
import ru.shulenin.farmownerapi.datasource.redis.repository.WorkerRedisRepository;
import ru.shulenin.farmownerapi.datasource.repository.ProductRepository;
import ru.shulenin.farmownerapi.datasource.repository.WorkerRepository;
import ru.shulenin.farmownerapi.dto.PlanReadDto;
import ru.shulenin.farmownerapi.dto.PlanSaveEditDto;
import ru.shulenin.farmownerapi.dto.PlanSendDto;

/**
 * Маппер для планов
 */
@Mapper
public interface PlanMapper {
    PlanMapper INSTANCE = Mappers.getMapper( PlanMapper.class );


    /**
     * От сущность к сообщению
     * @param plan сущность
     * @return сообщение
     */
    @Mapping(target = "workerId", source = "plan.worker.id")
    @Mapping(target = "productId", source = "plan.product.id")
    public PlanSendDto planToPlanSendDto(Plan plan);

    /**
     * От сущносоти к dto для чтения
     * @param plan сущность
     * @param workerMapper маппер для рабочих
     * @param productMapper маппер для продуктов
     * @return dto для чтения
     */
     default public PlanReadDto planToPlanReadDto(Plan plan,
                                         WorkerMapper workerMapper,
                                         ProductMapper productMapper) {
        var workerDto = workerMapper.workerToWorkerReadDto(plan.getWorker());
        var productDto = productMapper.productToReadDto(plan.getProduct());

        return new PlanReadDto(
                workerDto,
                productDto,
                plan.getAmount(),
                plan.getDate()
        );
    }

    /**
     * От dto для сохранения к сущность
     * @param planDto dto для сохранения
     * @param workerRepository репозиторий для рабочих
     * @param productRepository репозиторий для продуктов
     * @return
     */
    @Transactional(readOnly = true)
    default public Plan planSaveEditDtoToPlan(PlanSaveEditDto planDto,
                                       WorkerRedisRepository workerRepository,
                                       ProductRedisRepository productRepository) {
         var worker = workerRepository.findById(planDto.getWorkerId());
         var product = productRepository.findById(planDto.getProductId());

         Plan plan = new Plan();

         worker.map(wrk -> {
             plan.setWorker(wrk);
             return wrk;
         }).orElseThrow(EntityNotFoundException::new);

         product.map(prod -> {
             plan.setProduct(prod);
             return prod;
         }).orElseThrow(EntityNotFoundException::new);

         plan.setAmount(planDto.getAmount());
         plan.setDate(planDto.getDate());

         return plan;
    }
}
