package br.com.educelulares.backend.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentDto(
        @NotNull
        Long id,
        @NotNull
        Long orderId,
        @NotNull
        @Positive
        BigDecimal amount,
        @NotBlank
        @NotNull
        String status,
        LocalDateTime paidAt

) {

}
