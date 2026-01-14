package io.github.aplaraujo.dto;

import io.github.aplaraujo.entities.enums.OrderStatus;

import java.util.List;

public record OrderDTO(Long id, Long clientId, List<ProductDTO> products, OrderStatus orderStatus) {
}
