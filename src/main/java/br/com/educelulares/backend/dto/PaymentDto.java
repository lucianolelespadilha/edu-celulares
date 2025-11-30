package br.com.educelulares.backend.dto;
import br.com.educelulares.backend.enums.PaymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;



public record PaymentDto(
        Long id,
        Long orderId,
        BigDecimal amount,
        PaymentStatus status,
        LocalDateTime paidAt
) { }

