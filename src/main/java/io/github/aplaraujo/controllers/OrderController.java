package io.github.aplaraujo.controllers;

import io.github.aplaraujo.dto.CreateOrderDTO;
import io.github.aplaraujo.dto.OrderDTO;
import io.github.aplaraujo.entities.Order;
import io.github.aplaraujo.mappers.OrderMapper;
import io.github.aplaraujo.security.UserDetailsImpl;
import io.github.aplaraujo.services.OrderService;
import io.github.aplaraujo.services.exceptions.OperationNotAllowedException;
import io.github.aplaraujo.services.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController implements GenericController {
    private final OrderService service;
    private final OrderMapper mapper;

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @GetMapping
    public ResponseEntity<List<OrderDTO>> findOrders(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long clientId = userDetails.getId();
        List<OrderDTO> orders = service.searchAllByClient(clientId);
        return ResponseEntity.ok(orders);
    }

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> findOrderById(@PathVariable("id") String id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long clientId = userDetails.getId();
        var orderId = Long.parseLong(id);
        Order order = service.searchOrderByIdAndUser(orderId, clientId);
        return ResponseEntity.ok(mapper.toDTO(order));
    }

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @PostMapping
    public ResponseEntity<OrderDTO> insertOrder(@RequestBody @Valid CreateOrderDTO dto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        OrderDTO created = service.insert(dto);
        var url = generateHeaderLocation(created.id());
        return ResponseEntity.created(url).build();
    }

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateOrder(@PathVariable("id") String id, @RequestBody @Valid CreateOrderDTO dto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        var orderId = Long.parseLong(id);
        Long clientId = userDetails.getId();
        service.update(orderId, clientId, dto);
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOrder(@PathVariable("id") String id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            var orderId = Long.parseLong(id);
            Long clientId = userDetails.getId();
            service.delete(orderId, clientId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (OperationNotAllowedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }
}
