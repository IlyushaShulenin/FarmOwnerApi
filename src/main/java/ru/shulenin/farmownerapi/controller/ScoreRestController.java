package ru.shulenin.farmownerapi.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.privilegedactions.IsClassPresent;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.shulenin.farmownerapi.dto.ScoreReadDto;
import ru.shulenin.farmownerapi.dto.ScoreSaveEditDto;
import ru.shulenin.farmownerapi.dto.WorkerReadDto;
import ru.shulenin.farmownerapi.dto.WorkerSaveEditDto;
import ru.shulenin.farmownerapi.exception.ThereAreNotEntities;
import ru.shulenin.farmownerapi.service.ScoreService;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/owner-api/v1/score")
@Slf4j
public class ScoreRestController {
    private final ScoreService scoreService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ScoreReadDto> findAll() {
        try {
            return scoreService.findAll();
        } catch (ThereAreNotEntities e) {
            log.warn("GET /owner-api/v1/score: there are not entities");
            return Collections.emptyList();
        }
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ScoreReadDto findById(@PathVariable("id") Long id) {
        return scoreService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ScoreReadDto save(@RequestBody ScoreSaveEditDto worker) {
        return scoreService.save(worker)
                .get();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean delete(@PathVariable("id") Long id) {
        try {
            scoreService.delete(id);
            return true;
        } catch (EntityNotFoundException e) {
            log.warn(String.format("ScoreRestController.delete(): score with id=%d does not exist", id));
            return false;
        }
    }

}
