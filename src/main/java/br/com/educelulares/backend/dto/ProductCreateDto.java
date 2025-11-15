package br.com.educelulares.backend.dto;

import java.math.BigDecimal;

public record ProductCreateDto(
        String name,
        BigDecimal price,
        String description
) {
}
