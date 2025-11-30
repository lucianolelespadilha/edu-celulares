package br.com.educelulares.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationCreateDto {
    @NotBlank
    private String keyName;
    @NotBlank
    private String value;
    @NotBlank
    private String description;
}
