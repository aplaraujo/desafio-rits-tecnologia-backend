package io.github.aplaraujo.controllers;

import io.github.aplaraujo.dto.ProductDTO;
import io.github.aplaraujo.entities.Product;
import io.github.aplaraujo.mappers.ProductMapper;
import io.github.aplaraujo.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController implements GenericController {
    private final ProductService service;
    private final ProductMapper mapper;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findProductById(@PathVariable("id") String id) {
        var productId = Long.parseLong(id);
        return service.findById(productId).map(prod -> {
            var dto = mapper.toDTO(prod);
            return ResponseEntity.ok(dto);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Void> insertProduct(@RequestBody @Valid ProductDTO dto) {
        service.insert(dto);
        var url = generateHeaderLocation(dto.id());
        return ResponseEntity.created(url).build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable("id") String id, @RequestBody @Valid ProductDTO dto) {
        var productId = Long.parseLong(id);
        return service.findById(productId).map(prod -> {
            prod.setName(dto.name());
            prod.setPrice(dto.price());
            service.update(prod);
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") String id) {
        var productId = Long.parseLong(id);
        return service.findById(productId).map(prod -> {
            service.delete(prod);
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
