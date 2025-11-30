package br.com.educelulares.backend.dto;
import br.com.educelulares.backend.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PaymentCreateDto(

        @NotNull(message = "orderId is required")
        @Positive(message = "orderId must be positive")
        Long orderId,

        @NotNull(message = "amount is required")
        @Positive(message = "amount must be greater than zero")
        BigDecimal amount,


        PaymentStatus status
) {}

