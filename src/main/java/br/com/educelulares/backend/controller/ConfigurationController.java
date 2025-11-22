package br.com.educelulares.backend.controller;

import br.com.educelulares.backend.dto.ConfigurationCreateDto;
import br.com.educelulares.backend.dto.ConfigurationResponseDto;
import br.com.educelulares.backend.dto.ConfigurationUpdateDto;
import br.com.educelulares.backend.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/configurations")
public class ConfigurationController {

    // INJEÇÃO DO SERVIÇO RESPONSÁVEL PELO GERENCIAMENTO DE CONFIGURAÇÕES
    @Autowired
    private ConfigurationService configurationService;

    // -------------------------------------------------------------------------
    // CRIA UMA NOVA CONFIGURAÇÃO
    // RECEBE UM DTO, DELEGA PARA O SERVICE E RETORNA HTTP 200 (OK)
    // -------------------------------------------------------------------------
    @PostMapping
    public ResponseEntity<ConfigurationResponseDto> create(
            @RequestBody ConfigurationCreateDto configurationCreateDto) {

        return ResponseEntity.ok(configurationService.create(configurationCreateDto));
    }

    // -------------------------------------------------------------------------
    // ATUALIZA UMA CONFIGURAÇÃO EXISTENTE PELO KEYNAME
    // RECEBE O KEYNAME COMO PATH VARIABLE E OS NOVOS DADOS VIA DTO
    // -------------------------------------------------------------------------
    @PutMapping("/{keyName}")
    public ResponseEntity<ConfigurationResponseDto> update(
            @PathVariable String keyName,
            @RequestBody ConfigurationUpdateDto configurationUpdateDto) {

        return ResponseEntity.ok(configurationService.update(keyName, configurationUpdateDto));
    }

    // -------------------------------------------------------------------------
    // RETORNA UMA CONFIGURAÇÃO PELO KEYNAME
    // O SERVICE LANÇA EXCEÇÃO CASO A CONFIGURAÇÃO NÃO EXISTA
    // -------------------------------------------------------------------------
    @GetMapping("/{keyName}")
    public ResponseEntity<ConfigurationResponseDto> get(@PathVariable String keyName) {
        return ResponseEntity.ok(configurationService.getByKey(keyName));
    }

    // -------------------------------------------------------------------------
    // RETORNA TODAS AS CONFIGURAÇÕES CADASTRADAS
    // RETORNA HTTP 200 (OK) COM A LISTA DE DTOS
    // -------------------------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<ConfigurationResponseDto>> getAll() {
        return ResponseEntity.ok(configurationService.getAll());
    }
}
