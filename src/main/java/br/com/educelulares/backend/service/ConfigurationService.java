package br.com.educelulares.backend.service;

import br.com.educelulares.backend.dto.ConfigurationCreateDto;
import br.com.educelulares.backend.dto.ConfigurationResponseDto;
import br.com.educelulares.backend.dto.ConfigurationUpdateDto;
import br.com.educelulares.backend.entity.Configuration;
import br.com.educelulares.backend.exception.UnauthorizedException;
import br.com.educelulares.backend.repository.ConfigurationRepository;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// -------------------------------------------------------------------------
// SERVIÇO RESPONSÁVEL PELO GERENCIAMENTO DAS CONFIGURAÇÕES DO SISTEMA
// REALIZA OPERAÇÕES DE CRIAÇÃO, ATUALIZAÇÃO E CONSULTA DE CONFIGURAÇÕES
// -------------------------------------------------------------------------
@Service
@AllArgsConstructor
@NoArgsConstructor
public class ConfigurationService {

    // ---------------------------------------------------------------------
    // INJEÇÃO DO REPOSITÓRIO RESPONSÁVEL PELO ACESSO À ENTIDADE Configuration
    // ---------------------------------------------------------------------
    @Autowired
    private ConfigurationRepository configurationRepository;

    // ---------------------------------------------------------------------
    // CRIA UMA NOVA CONFIGURAÇÃO NO SISTEMA
    // ---------------------------------------------------------------------
    public ConfigurationResponseDto create(ConfigurationCreateDto configurationCreateDto) {

        // -------------------------------------------------------------
        // CRIA UMA NOVA INSTÂNCIA DA ENTIDADE Configuration
        // -------------------------------------------------------------
        Configuration config = new Configuration();
        config.setKeyName(configurationCreateDto.getKeyName());
        config.setValue(configurationCreateDto.getValue());
        config.setDescription(configurationCreateDto.getDescription());

        // -------------------------------------------------------------
        // SALVA A CONFIGURAÇÃO NO BANCO DE DADOS
        // -------------------------------------------------------------
        configurationRepository.save(config);

        // -------------------------------------------------------------
        // RETORNA A CONFIGURAÇÃO CRIADA EM FORMATO DTO
        // -------------------------------------------------------------
        return toResponse(config);
    }

    // ---------------------------------------------------------------------
    // ATUALIZA UMA CONFIGURAÇÃO EXISTENTE IDENTIFICADA PELO keyName
    // ---------------------------------------------------------------------
    public ConfigurationResponseDto update(String keyName, ConfigurationUpdateDto configurationUpdateDto) {

        // -------------------------------------------------------------
        // BUSCA A CONFIGURAÇÃO PELO KEYNAME OU RETORNA ERRO SE NÃO EXISTIR
        // -------------------------------------------------------------
        Configuration config = configurationRepository.findByKeyName(keyName)
                .orElseThrow(() -> new UnauthorizedException("CONFIGURATION NOT FOUND"));

        // -------------------------------------------------------------
        // ATUALIZA OS CAMPOS MODIFICADOS
        // -------------------------------------------------------------
        config.setValue(configurationUpdateDto.getValue());
        config.setDescription(configurationUpdateDto.getDescription());

        // -------------------------------------------------------------
        // SALVA AS ALTERAÇÕES NO BANCO DE DADOS
        // -------------------------------------------------------------
        configurationRepository.save(config);

        // -------------------------------------------------------------
        // RETORNA A CONFIGURAÇÃO ATUALIZADA EM DTO
        // -------------------------------------------------------------
        return toResponse(config);
    }

    // ---------------------------------------------------------------------
    // RETORNA UMA CONFIGURAÇÃO ESPECÍFICA PELO KEYNAME
    // ---------------------------------------------------------------------
    public ConfigurationResponseDto getByKey(String keyName) {

        // -------------------------------------------------------------
        // BUSCA A CONFIGURAÇÃO OU RETORNA ERRO SE NÃO FOR ENCONTRADA
        // -------------------------------------------------------------
        Configuration config = configurationRepository.findByKeyName(keyName)
                .orElseThrow(() -> new UnauthorizedException("CONFIGURATION NOT FOUND"));

        // -------------------------------------------------------------
        // RETORNA O DTO CORRESPONDENTE
        // -------------------------------------------------------------
        return toResponse(config);
    }

    // ---------------------------------------------------------------------
    // RETORNA TODAS AS CONFIGURAÇÕES CADASTRADAS NO SISTEMA
    // ---------------------------------------------------------------------
    public List<ConfigurationResponseDto> getAll() {
        return configurationRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ---------------------------------------------------------------------
    // CONVERTE A ENTIDADE Configuration PARA O DTO Response
    // ---------------------------------------------------------------------
    private ConfigurationResponseDto toResponse(Configuration config) {

        // -------------------------------------------------------------
        // CRIA O DTO E PREENCHE COM OS DADOS DA ENTIDADE
        // -------------------------------------------------------------
        ConfigurationResponseDto responseDto = new ConfigurationResponseDto();
        responseDto.setId(config.getId());
        responseDto.setKeyName(config.getKeyName());
        responseDto.setValue(config.getValue());
        responseDto.setDescription(config.getDescription());

        return responseDto;
    }
}