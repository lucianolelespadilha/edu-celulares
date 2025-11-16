package br.com.educelulares.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductCreateDto(
        @NotBlank
        String name,
        @NotNull
        BigDecimal price,
        @NotBlank
        String description
) {
}
