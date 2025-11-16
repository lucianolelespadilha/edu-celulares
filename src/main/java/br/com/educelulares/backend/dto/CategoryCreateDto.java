package br.com.educelulares.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryCreateDto(
        @NotBlank
        String name,
        @Size(max = 255)
        String description
) { }
