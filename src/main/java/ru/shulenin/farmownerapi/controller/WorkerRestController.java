package ru.shulenin.farmownerapi.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.shulenin.farmownerapi.dto.WorkerReadDto;
import ru.shulenin.farmownerapi.dto.WorkerSaveEditDto;
import ru.shulenin.farmownerapi.exception.ThereAreNotEntities;
import ru.shulenin.farmownerapi.service.WorkerService;

import java.util.Collections;
import java.util.List;

/**
 * Контроллер для работы с рабочими
 */
@RestController
@RequestMapping("/owner-api/v1/worker")
@RequiredArgsConstructor
@Slf4j
public class WorkerRestController {

    private final WorkerService workerService;

    /**
     * Получить всех рабочих
     * @return список рабочих
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<WorkerReadDto> findAll() {
        try {
            return workerService.findAll();
        } catch (ThereAreNotEntities e) {
            log.warn("GET /owner-api/v1/worker: there are no entities");
            return Collections.emptyList();
        }
    }

    /**
     * Получить рабочего по id
     * @param id id рабочего
     * @return dto рабочего для чтения
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public WorkerReadDto findById(@PathVariable("id") Long id) {
        return workerService.findById(id)
                .orElseThrow(() -> {
                    log.warn(String.format("GET /owner-api/v1/worker/%d: there is no entity", id));
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
    }

    /**
     * Сохранение рабочего
     * @param worker dto рабочего для сохранения
     * @return
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkerReadDto save(@RequestBody @Valid WorkerSaveEditDto worker) {
        return workerService.save(worker)
                .get();
    }

    /**
     * Удаление рабочего по id
     * @param id id рабочего
     * @return true если рабочий успешно удален, иначе false
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean delete(@PathVariable("id") Long id)  {
        try {
            workerService.delete(id);
            return  true;
        } catch (EntityNotFoundException e) {
            log.warn(String.format("DELETE /owner-api/v1/worker/%d: there is no entity", id));
            return false;
        }
    }
}
