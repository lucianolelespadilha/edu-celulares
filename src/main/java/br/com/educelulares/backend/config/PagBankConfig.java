package br.com.educelulares.backend.config;

import lombok.Getter;
import lombok.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.http.HttpHeaders;

@Configuration
@Getter
public class PagBankConfig {

    @Value("${pagbank.base-url}")
    private String baseUrl;
    @Value("${pagbank.client-id}")
    private String clientId;
    @Value("${pagbank.client-secret}")
    private String clientSecret;

    @Bean
   public WebClient pagBankClient(){
       return WebClient.builder()
               .baseUrl(baseUrl)
               .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
               .build();
   }


}
