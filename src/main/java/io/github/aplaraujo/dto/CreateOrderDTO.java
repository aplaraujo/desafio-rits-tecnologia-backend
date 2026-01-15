package io.github.aplaraujo.dto;

import io.github.aplaraujo.entities.enums.OrderStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record CreateOrderDTO(
        @Positive(message = "This id should be positive")
        Long clientId,

        @NotEmpty(message = "This array should have at least one product")
        List<ProductDTO> products,

        @NotNull(message = "This field should not be null")
        OrderStatus orderStatus
) {
}
