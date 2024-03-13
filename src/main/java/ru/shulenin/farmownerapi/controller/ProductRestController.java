package ru.shulenin.farmownerapi.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.shulenin.farmownerapi.dto.ProductReadDto;
import ru.shulenin.farmownerapi.dto.ProductSaveEditDto;
import ru.shulenin.farmownerapi.exception.ThereAreNotEntities;
import ru.shulenin.farmownerapi.service.ProductService;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/owner-api/v1/products")
@RequiredArgsConstructor
@Slf4j
public class ProductRestController {
    private final ProductService productService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductReadDto> findAll() {
        try {
            return productService.findAll();
        } catch(ThereAreNotEntities e) {
            log.warn("GET /owner-api/v1/products: there are no entities");
            return Collections.emptyList();
        }
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductReadDto findById(@PathVariable("id") Long id) {
        return productService.findById(id)
                .orElseThrow(() -> {
                    log.warn(String.format("GET /owner-api/v1/products/%d: there is no entity", id));
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductReadDto save(@RequestBody @Valid ProductSaveEditDto productDto) {
        return productService.save(productDto)
                .get();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean delete(@PathVariable("id") Long id) {
        try {
            productService.delete(id);
            return true;
        } catch(EntityNotFoundException e) {
            log.warn(String.format("DELETE /owner-api/v1/product/%d: there is no entity", id));
            return false;
        }
    }
}
