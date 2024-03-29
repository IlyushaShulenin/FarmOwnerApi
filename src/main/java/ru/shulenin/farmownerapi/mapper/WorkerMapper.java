package ru.shulenin.farmownerapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.shulenin.farmownerapi.datasource.entity.Worker;
import ru.shulenin.farmownerapi.dto.WorkerReadDto;
import ru.shulenin.farmownerapi.dto.WorkerSaveEditDto;
import ru.shulenin.farmownerapi.dto.WorkerSendDto;


/**
 * Маппер для рабочего
 */
@Mapper
public interface WorkerMapper {
    WorkerMapper INSTANCE = Mappers.getMapper( WorkerMapper.class );

    /**
     * От сущности к dto для чтения
     * @param worker сущность
     * @return dto для чтения
     */
    public WorkerReadDto workerToWorkerReadDto(Worker worker);

    /**
     * От сущности к сообщению
     * @param worker сущность
     * @return сообщение
     */
    public WorkerSendDto workerToWorkerSendDto(Worker worker);

    /**
     * От dto для сохранения к сущности
     * @param worker dto для сохранения
     * @return сущность
     */
    default  public Worker workerSaveEditDtoToWorker(WorkerSaveEditDto worker, PasswordEncoder passwordEncoder) {
        return new Worker(
                worker.getEmail(),
                passwordEncoder.encode(worker.getPassword()),
                worker.getName(),
                worker.getSurname()
        );
    }
}
