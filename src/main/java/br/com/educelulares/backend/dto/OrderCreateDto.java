package br.com.educelulares.backend.dto;

import java.util.List;

public record OrderCreateDto(
        Long costumerId,
        List<OrderItemCreateDto> items

) {
}
