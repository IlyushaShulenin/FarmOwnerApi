package ru.shulenin.farmownerapi.mapper;

import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.transaction.annotation.Transactional;
import ru.shulenin.farmownerapi.datasource.entity.Plan;
import ru.shulenin.farmownerapi.datasource.repository.ProductRepository;
import ru.shulenin.farmownerapi.datasource.repository.WorkerRepository;
import ru.shulenin.farmownerapi.dto.PlanReadDto;
import ru.shulenin.farmownerapi.dto.PlanSaveEditDto;
import ru.shulenin.farmownerapi.dto.PlanSendDto;

@Mapper
public interface PlanMapper {
    PlanMapper INSTANCE = Mappers.getMapper( PlanMapper.class );


    @Mapping(target = "workerId", source = "plan.worker.id")
    @Mapping(target = "productId", source = "plan.product.id")
    public PlanSendDto planToPlanSendDto(Plan plan);

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

    @Transactional(readOnly = true)
    default public Plan planSaveEditDtoToPlan(PlanSaveEditDto planDto,
                                       WorkerRepository workerRepository,
                                       ProductRepository productRepository) {
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
