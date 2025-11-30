package br.com.educelulares.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;


/// -----------------------------------------------------------------------------
// CONFIGURAÇÃO DO WEBCLIENT PARA INTEGRAÇÃO COM A API DO PAGBANK
// ESTA CLASSE LÊ AS CREDENCIAIS DO ARQUIVO application.properties
// E EXPÕE UM BEAN WebClient PRONTO PARA USO EM QUALQUER SERVICE.
// -----------------------------------------------------------------------------
@Configuration
public class PagBankConfig {

    // -------------------------------------------------------------------------
    // URL BASE DO PAGBANK (SANDBOX OU PRODUÇÃO)
    // EXEMPLO (SANDBOX): https://sandbox.api.pagseguro.com
    // -------------------------------------------------------------------------
    @Value("${pagbank.base-url}")
    private String baseUrl;

    // -------------------------------------------------------------------------
    // APP ID DO SANDBOX DO PAGBANK
    // FORNECIDO AUTOMATICAMENTE PELO PAINEL SANDBOX
    // É ENVIADO NO HEADER x-client-id
    // -------------------------------------------------------------------------
    @Value("${pagbank.app-id}")
    private String appId;

    // -------------------------------------------------------------------------
    // APP KEY DO SANDBOX DO PAGBANK
    // USADO COMO TOKEN DE AUTENTICAÇÃO NO HEADER Authorization: Bearer
    // -------------------------------------------------------------------------
    @Value("${pagbank.app-key}")
    private String appKey;

    // -------------------------------------------------------------------------
    // BEAN WEBCLIENT
    // CONFIGURADO COM:
    // 1. BASE URL DO PAGBANK
    // 2. CONTENT-TYPE APPLICATION/JSON
    // 3. AUTENTICAÇÃO OBRIGATÓRIA:
    //      - x-client-id: APP ID
    //      - Authorization: Bearer APP KEY
    //
    // OBS: SEM ESSES HEADERS, TODAS AS REQUISIÇÕES RETORNAM 401
    // -------------------------------------------------------------------------
    @Bean
    public WebClient pagBankWebClient() {

        return WebClient.builder()
                // URL raiz da API PagBank
                .baseUrl(baseUrl)

                // Informando que o conteúdo enviado/recebido é JSON
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

                // Header obrigatório reconhecido pelo PagBank
                .defaultHeader("x-client-id", appId)

                // Token de autenticação (APP KEY)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + appKey)

                .build();
    }
}
