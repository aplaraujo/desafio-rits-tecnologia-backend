package io.github.aplaraujo.controllers;

import io.github.aplaraujo.dto.ProductDTO;
import io.github.aplaraujo.entities.Product;
import io.github.aplaraujo.mappers.ProductMapper;
import io.github.aplaraujo.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/catalog/products")
@RequiredArgsConstructor
public class CatalogProductController {
    private final ProductService service;
    private final ProductMapper mapper;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findProducts(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "page-size", defaultValue = "12") Integer pageSize
    ) {
        Page<Product> products = service.search(name, page, pageSize);
        Page<ProductDTO> result = products.map(mapper::toDTO);
        return ResponseEntity.ok(result);
    }
}
