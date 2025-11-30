package br.com.educelulares.backend.dto;

public record CustomerDto(
        @NotNull(message = "Customer ID is required")
        Long id,

        @NotBlank(message = "Customer name is required")
        String name,

        @NotBlank(message = "Customer email is required")
        @Email(message = "Invalid email format")
        String email
) { }

