package br.com.educelulares.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record CustomerCreateDto(
        @NotBlank
        String email,
        @NotBlank
        String name
) { }
