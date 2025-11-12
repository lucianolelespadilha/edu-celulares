package br.com.educelulares.backend.dto;

public record OrderItemCreateDto(
        Long productId,
        Integer quantity
) {}
