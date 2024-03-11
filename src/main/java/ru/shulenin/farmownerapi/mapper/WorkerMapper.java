package ru.shulenin.farmownerapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.shulenin.farmownerapi.datasource.entity.Worker;
import ru.shulenin.farmownerapi.dto.WorkerReadDto;
import ru.shulenin.farmownerapi.dto.WorkerSaveEditDto;
import ru.shulenin.farmownerapi.dto.WorkerSendDto;


@Mapper
public interface WorkerMapper {
    WorkerMapper INSTANCE = Mappers.getMapper( WorkerMapper.class );

    public WorkerReadDto workerToWorkerReadDto(Worker worker);
    public WorkerSendDto workerToWorkerSendDto(Worker worker);
    public Worker workerSaveEditdtoToWorker(WorkerSaveEditDto worker);
}
