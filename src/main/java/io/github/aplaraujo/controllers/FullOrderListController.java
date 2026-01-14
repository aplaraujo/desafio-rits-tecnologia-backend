package io.github.aplaraujo.controllers;

import io.github.aplaraujo.dto.OrderDTO;
import io.github.aplaraujo.entities.Order;
import io.github.aplaraujo.entities.Product;
import io.github.aplaraujo.mappers.OrderMapper;
import io.github.aplaraujo.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order-list")
@RequiredArgsConstructor
public class FullOrderListController {

    private final OrderService service;
    private final OrderMapper mapper;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<OrderDTO>> findOrdersFull(
            @RequestParam(value = "product", required = false) Product product,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "page-size", defaultValue = "12") Integer pageSize
    ) {
        Page<Order> result = service.search(product, page, pageSize);
        Page<OrderDTO> page1 = result.map(mapper::toDTO);
        return ResponseEntity.ok(page1);
    }
}
