package br.com.educelulares.backend.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentDto(
        Long id,
        Long orderId,
        BigDecimal amount,
        String status,
        LocalDateTime paidAt

) {

}
