package br.com.educelulares.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;


// -----------------------------------------------------------------------------
// CONFIGURAÇÃO DO CLIENTE HTTP (WEBCLIENT) PARA COMUNICAÇÃO COM A API PAGBANK
// ESTA CLASSE CARREGA AS CREDENCIAIS DO ARQUIVO application.properties
// E EXPÕE UM BEAN QUE PODE SER INJETADO EM QUALQUER SERVICE
// -----------------------------------------------------------------------------
@Configuration
public class PagBankConfig {

    // -------------------------------------------------------------------------
    // URL BASE DA API PAGBANK (SANDBOX OU PRODUÇÃO)
    // CARREGADA AUTOMATICAMENTE DO application.properties
    // -------------------------------------------------------------------------
    @Value("${pagbank.base-url}")
    private String baseUrl;

    // -------------------------------------------------------------------------
    // CLIENT ID DO PAGBANK
    // IDENTIFICA SUA APLICAÇÃO DENTRO DO PAGBANK
    // -------------------------------------------------------------------------
    @Value("${pagbank.client-id}")
    private String clientId;

    // -------------------------------------------------------------------------
    // CLIENT SECRET DO PAGBANK
    // UTILIZADO PARA AUTENTICAÇÃO NAS REQUISIÇÕES
    // -------------------------------------------------------------------------
    @Value("${pagbank.client-secret}")
    private String clientSecret;

    // -------------------------------------------------------------------------
    // CRIA E CONFIGURA O WEBCLIENT QUE SERÁ UTILIZADO PARA CHAMAR A API PAGBANK
    // DEFINIÇÃO DO CONTENT-TYPE COMO JSON E DEFINIÇÃO DA BASE URL
    // ESTE WEBCLIENT SERÁ INJETADO EM SERVIÇOS COMO: PagBankService
    // -------------------------------------------------------------------------
    @Bean
    public WebClient pagBankWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
