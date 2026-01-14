package io.github.aplaraujo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductDTO(
        Long id,

        @NotBlank(message = "This field should not be empty")
        @Size(min = 3, max = 100, message = "The name should have from 3 to 100 characters")
        String name,

        @Positive(message = "The price should be positive")
        Double price) {
}
