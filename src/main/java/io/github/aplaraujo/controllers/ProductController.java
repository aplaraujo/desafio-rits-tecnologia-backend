package io.github.aplaraujo.controllers;

import io.github.aplaraujo.dto.ProductDTO;
import io.github.aplaraujo.entities.Product;
import io.github.aplaraujo.mappers.ProductMapper;
import io.github.aplaraujo.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;
    private final ProductMapper mapper;

//    @GetMapping
//    public ResponseEntity<Page<ProductDTO>> findProducts(
//            @RequestParam(value = "name", required = false) String name,
//            @RequestParam(value = "page", defaultValue = "0") Integer page,
//            @RequestParam(value = "page-size", defaultValue = "12") Integer pageSize
//    ) {
//        Page<Product> products = service.search(name, page, pageSize);
//        Page<ProductDTO> result = products.map(mapper::toDTO);
//        return ResponseEntity.ok(result);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findProductById(@PathVariable("id") String id) {
        var productId = Long.parseLong(id);
        return service.findById(productId).map(prod -> {
            var dto = mapper.toDTO(prod);
            return ResponseEntity.ok(dto);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }


}
