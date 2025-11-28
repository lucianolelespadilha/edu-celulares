package br.com.educelulares.backend.dto;
import br.com.educelulares.backend.enums.PaymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

import java.time.LocalDateTime;

public record PaymentCreateDto (
        @NotNull
        Long id,
        @NotNull
        Long orderId,
        @NotNull
        @Positive
        BigDecimal amount,

        PaymentStatus status,
        LocalDateTime paidAt
){}
