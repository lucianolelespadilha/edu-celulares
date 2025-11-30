package br.com.educelulares.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductCreateDto(

        @NotBlank(message = "Product name is required")
        @Size(min = 2, max = 120, message = "Product name must be between 2 and 120 characters")
        String name,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be greater than zero")
        BigDecimal price,

        @NotBlank(message = "Product description is required")
        @Size(min = 5, max = 500, message = "Description must be between 5 and 500 characters")
        String description

) { }
