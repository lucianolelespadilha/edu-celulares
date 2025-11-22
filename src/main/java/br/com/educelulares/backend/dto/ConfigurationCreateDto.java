package br.com.educelulares.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationCreateDto {

    private String keyName;
    private String value;
    private String description;
}
