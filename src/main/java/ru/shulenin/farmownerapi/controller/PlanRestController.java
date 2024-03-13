package ru.shulenin.farmownerapi.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.shulenin.farmownerapi.dto.PlanReadDto;
import ru.shulenin.farmownerapi.dto.PlanSaveEditDto;
import ru.shulenin.farmownerapi.exception.ThereAreNotEntities;
import ru.shulenin.farmownerapi.service.PlanService;

import java.util.Collections;
import java.util.List;

/**
 * Контроллер для планов
 */
@RestController
@RequestMapping("/owner-api/v1/plan")
@RequiredArgsConstructor
@Slf4j
public class PlanRestController {
    private final PlanService planService;

    /**
     * Вернуть все планы
     * @return список планов
     * @throws ThereAreNotEntities
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PlanReadDto> findAll() throws ThereAreNotEntities {
        try {
            return planService.findAll();
        } catch(ThereAreNotEntities e) {
            log.warn("GET /owner-api/v1/plan: there are no entities");
            return Collections.emptyList();
        }
    }

    /**
     * Найти план по id
     * @param id id плана
     * @return dto плана для чтения
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlanReadDto findById(@PathVariable Long id) {
        return planService.findById(id)
                .orElseThrow(() -> {
                    log.warn(String.format("GET /owner-api/v1/plan/%d: there is no entity", id));
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
    }


    /**
     * Сохранение плана
     * @param planDto dto плана для сохранения
     * @return dto плана для чтения
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlanReadDto save(@RequestBody @Valid PlanSaveEditDto planDto) {
        return planService.save(planDto)
                .get();
    }

    /**
     * Удаление плана по id
     * @param id id плана
     * @return true если план успешно удален, иначе false
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean delete(@PathVariable Long id) {
        try {
            planService.delete(id);
            return true;
        } catch(EntityNotFoundException e) {
            log.warn(String.format("DELETE /owner-api/v1/plan/%d: there is no entity", id));
            return false;
        }
    }
}
