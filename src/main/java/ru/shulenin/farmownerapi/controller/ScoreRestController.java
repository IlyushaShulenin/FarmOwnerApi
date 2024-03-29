package ru.shulenin.farmownerapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.shulenin.farmownerapi.dto.ScoreReadDto;
import ru.shulenin.farmownerapi.dto.ScoreSaveEditDto;
import ru.shulenin.farmownerapi.service.ScoreService;

import java.util.List;

/**
 * Контроллер для баллов
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/owner-api/v1/score")
@Slf4j
public class ScoreRestController {
    private final ScoreService scoreService;

    /**
     * Получить все баллов
     * @return список баллов
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ScoreReadDto> findAll() {
        return scoreService.findAll();
    }

    /**
     * Получить балл по id
     * @param id id балла
     * @return dto баллов для чтения
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ScoreReadDto findById(@PathVariable("id") Long id) {
        return scoreService.findById(id)
                .orElseThrow(() -> {
                    log.warn(String.format("GET /owner-api/v1/score/%d: there is no entity", id));
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
    }

    /**
     * Сохранение баллов
     * @param scoreDto dto баллов для сохранения
     * @return dto баллов для чтения
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ScoreReadDto save(@RequestBody @Valid ScoreSaveEditDto scoreDto) {
        return scoreService.save(scoreDto)
                .orElseThrow(() -> {
                    log.warn(String.format("POST /owner-api/v1/score: %s wrong data", scoreDto));
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
    }

    /**
     * Удаление баллов по id
     * @param id id балла
     * @return true если баллы успешно удалены, иначе false
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        if (!scoreService.delete(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

}
