package br.com.educelulares.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CustomerDto(
        @NotNull(message = "Customer ID is required")
        Long id,

        @NotBlank(message = "Customer name is required")
        String name,

        @NotBlank(message = "Customer email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "O CPF/CNPJ é obrigatório.")
        @Pattern(
                regexp = "\\d{11}|\\d{14}",
                message = "O CPF/CNPJ deve conter 11 (CPF) ou 14 dígitos (CNPJ), apenas números."
        )
        String cpf
) { }

