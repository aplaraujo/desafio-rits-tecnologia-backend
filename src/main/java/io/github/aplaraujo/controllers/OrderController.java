package io.github.aplaraujo.controllers;

import io.github.aplaraujo.dto.OrderDTO;
import io.github.aplaraujo.entities.Order;
import io.github.aplaraujo.mappers.OrderMapper;
import io.github.aplaraujo.security.UserDetailsImpl;
import io.github.aplaraujo.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;
    private final OrderMapper mapper;

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @GetMapping
    public ResponseEntity<List<OrderDTO>> findOrders(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long clientId = userDetails.getId();
        List<OrderDTO> orders = service.searchAllByClient(clientId);
        return ResponseEntity.ok(orders);
    }
}
