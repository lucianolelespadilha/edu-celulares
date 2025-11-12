package br.com.educelulares.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDto(
Long id,
Long customerId,
List<OrderItemDto> itens,
LocalDateTime createdAt
) {}
