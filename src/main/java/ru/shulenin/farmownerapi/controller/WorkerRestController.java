package ru.shulenin.farmownerapi.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.shulenin.farmownerapi.dto.WorkerReadDto;
import ru.shulenin.farmownerapi.dto.WorkerSaveEditDto;
import ru.shulenin.farmownerapi.exception.ThereAreNotEntities;
import ru.shulenin.farmownerapi.service.WorkerService;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/worker")
@RequiredArgsConstructor
@Slf4j
public class WorkerRestController {

    private final WorkerService workerService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<WorkerReadDto> findAll() {
        try {
            return workerService.findAll();
        } catch (ThereAreNotEntities e) {
            log.warn("GET /api/v1/worker: there are not entities");
            return Collections.emptyList();
        }
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public WorkerReadDto findById(@PathVariable("id") Long id) {
        return workerService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkerReadDto save(@RequestBody WorkerSaveEditDto worker) {
        return workerService.save(worker)
                .get();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean delete(@PathVariable("id") Long id) {
        try {
            workerService.delete(id);
            return true;
        } catch (EntityNotFoundException e) {
            log.warn(String.format("WorkerRestController.delete(): worker with id=%d does not exist", id));
            return false;
        }
    }
}
