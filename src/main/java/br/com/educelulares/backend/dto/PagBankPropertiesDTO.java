package br.com.educelulares.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;

/**
 * DTO RESPONSÁVEL POR TRANSPORTAR DADOS DE CONFIGURAÇÃO DO PAGBANK
 * ENTRE O CONTROLLER E O SERVICE.
 *
 * ESTE DTO NÃO É A MESMA COISA QUE A CLASSE DE CONFIGURAÇÃO
 * (PagBankProperties), QUE LÊ O application.properties.
 *
 * ESTE OBJETO É USADO APENAS PARA ENTRADAS E SAÍDAS DA API.
 */
@Getter
@Setter
@RequiredArgsConstructor
public class PagBankPropertiesDTO {

    /**
     * URL BASE DAS REQUISIÇÕES AO PAGBANK.
     * NORMALMENTE ESTE VALOR NÃO É ALTERADO PELO CLIENTE.
     */
    @NotBlank(message = "Base URL cannot be empty")
    private String baseUrl;

    /**
     * API KEY OU CLIENT_SECRET FORNECIDO PELO PAGBANK.
     * ESTE VALOR É SENSÍVEL E NÃO DEVE SER EXPOSTO EM RESPOSTAS.
     */
    @NotBlank(message = "API Key cannot be empty")
    private String apiKey;

    /**
     * CLIENT_ID UTILIZADO NO FLUXO DE AUTENTICAÇÃO.
     * ASSOCIADO AO APLICATIVO REGISTRADO NO PAGBANK.
     */
    @NotBlank(message = "Client ID cannot be empty")
    private String clientId;
}
