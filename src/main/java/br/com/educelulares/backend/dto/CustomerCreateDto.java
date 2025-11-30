package br.com.educelulares.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CustomerCreateDto(

        @NotBlank(message = "O nome é obrigatório.")
        String name,

        @NotBlank(message = "O email é obrigatório.")
        @Email(message = "Formato de email inválido.")
        String email,

        @NotBlank(message = "O CPF/CNPJ é obrigatório.")
        @Pattern(
                regexp = "\\d{11}|\\d{14}",
                message = "O CPF/CNPJ deve conter 11 (CPF) ou 14 dígitos (CNPJ), apenas números."
        )
        String cpf

) { }

