package br.com.educelulares.backend.dto;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationResponseDto {

    private Long id;
    @NotBlank
    private String keyName;
    @NotBlank
    private String value;
    @NotBlank
    private String description;
}
