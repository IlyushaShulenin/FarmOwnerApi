package ru.shulenin.farmownerapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.shulenin.farmownerapi.dto.PlanReadDto;
import ru.shulenin.farmownerapi.dto.PlanSaveEditDto;
import ru.shulenin.farmownerapi.exception.ThereAreNotEntities;
import ru.shulenin.farmownerapi.service.PlanService;

import java.util.List;

@RestController
@RequestMapping("/owner-api/v1/plan")
@RequiredArgsConstructor
public class PlanController {
    private final PlanService planService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PlanReadDto> findAll() throws ThereAreNotEntities {
        return planService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlanReadDto findById(@PathVariable Long id) {
        return planService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlanReadDto save(PlanSaveEditDto planDto) {
        return planService.save(planDto)
                .get();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        planService.delete(id);
    }
}
