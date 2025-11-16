package br.com.educelulares.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderCreateDto(
        @NotNull
        Long costumerId,
        @NotEmpty
        @Valid
        List<OrderItemCreateDto> items

) {
}
