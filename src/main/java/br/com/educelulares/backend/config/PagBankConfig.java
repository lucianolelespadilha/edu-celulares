package br.com.educelulares.backend.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PagBankConfig {

    @Value("${pagbank.base-url}")
    private String baseUrl;

    // ALTERAÇÃO IMPORTANTE AQUI ↓↓↓
    @Value("${pagbank.app-key}")
    private String token;

    @PostConstruct
    public void debug() {
        log.info("===== PAGBANK CONFIG =====");
        log.info("BASE URL = {}", baseUrl);
        log.info("TOKEN    = {}", (token != null && token.length() > 5) ? "OK" : "VAZIO");
        log.info("==========================");
    }

    @Bean
    public WebClient pagBankWebClient(WebClient.Builder builder) {

        ExchangeFilterFunction authFilter = ExchangeFilterFunction.ofRequestProcessor(request -> {
            ClientRequest newReq = ClientRequest.from(request)
                    .header("Authorization", "Bearer " + token)
                    .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                    .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .build();
            return reactor.core.publisher.Mono.just(newReq);
        });

        return builder
                .baseUrl(baseUrl)
                .filter(authFilter)
                .build();
    }
}
