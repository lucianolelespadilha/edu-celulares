package br.com.educelulares.backend.dto;

public record OrderItemCreateDto(
       Long orderId,
        Long productId,
        Integer quantity
) {}
