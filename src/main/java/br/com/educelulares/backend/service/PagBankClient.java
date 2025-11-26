package br.com.educelulares.backend.service;

import br.com.educelulares.backend.dto.PagBankOrderRequestDto;
import br.com.educelulares.backend.dto.PagBankOrderResponseDto;
import br.com.educelulares.backend.dto.PagBankPaymentResponseDto;
import br.com.educelulares.backend.exception.BadRequestException;
import br.com.educelulares.backend.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
@RequiredArgsConstructor
public class PagBankClient {

    // -----------------------------------------------------------------------------
    // WEBCLIENT INJETADO A PARTIR DA CLASSE PagBankConfig
    // ESTE É O CLIENTE HTTP UTILIZADO PARA CHAMAR A API DO PAGBANK
    // ELE JÁ POSSUI BASE URL E HEADERS PADRÕES DEFINIDOS NA CONFIGURAÇÃO
    // -----------------------------------------------------------------------------
    private final WebClient pagBankClient;


    // -----------------------------------------------------------------------------
    // MÉTODO RESPONSÁVEL POR CRIAR UMA ORDEM DE PAGAMENTO PIX NO PAGBANK
    // ELE ENVIA UMA REQUISIÇÃO POST PARA O ENDPOINT /orders
    // RECEBE UM PagBankOrderRequestDto E RETORNA PagBankOrderResponseDto
    // -----------------------------------------------------------------------------
    public PagBankOrderResponseDto createPixOrder(PagBankOrderRequestDto pagBankOrderRequestDto) {

        return pagBankClient.post()
                // ENDPOINT PARA CRIAÇÃO DE ORDEM DE PAGAMENTO
                .uri("/orders")

                // OBJETO QUE REPRESENTA A ORDEM A SER ENVIADA AO PAGBANK
                .bodyValue(pagBankOrderRequestDto)

                // EXECUTA A REQUISIÇÃO E PREPARA O MANUSEIO DE STATUS/ERROS
                .retrieve()

                // -----------------------------------------------------------------------------
                // TRATAMENTO DE ERROS HTTP DO TIPO 4xx (ERROS DO CLIENTE)
                // AQUI PODE SER FALTA DE AUTENTICAÇÃO, REQUEST INVÁLIDO, ETC.
                // -----------------------------------------------------------------------------
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        response.bodyToMono(String.class)  // LÊ O CORPO DA RESPOSTA DE ERRO
                                .map(body -> {
                                    // ERRO ESPECÍFICO 401
                                    if (response.statusCode().value() == 401) {
                                        return new UnauthorizedException("Unauthorized PagBank: " + body);
                                    }
                                    // QUALQUER OUTRO 4xx
                                    return new BadRequestException("PagBank 4xx error: " + body);
                                })
                )

                // -----------------------------------------------------------------------------
                // TRATAMENTO DE ERROS HTTP DO TIPO 5xx (ERROS DO SERVIDOR PAGBANK)
                // SIGNIFICA QUE O SERVIÇO DO PAGBANK FALHOU AO PROCESSAR A REQUISIÇÃO
                // -----------------------------------------------------------------------------
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        response.bodyToMono(String.class)
                                .map(body ->
                                        new BadRequestException("PagBank server error: " + body)
                                )
                )

                // -----------------------------------------------------------------------------
                // EM CASO DE SUCESSO, CONVERTE A RESPOSTA JSON NO DTO PagBankOrderResponseDto
                // -----------------------------------------------------------------------------
                .bodyToMono(PagBankOrderResponseDto.class)

                // -----------------------------------------------------------------------------
                // EXECUTA O FLUXO DE MANEIRA SINCRONA (O RESTO DO SEU PROJETO NÃO É REATIVO)
                // -----------------------------------------------------------------------------
                .block();
    }


    // -----------------------------------------------------------------------------
    // MÉTODO RESPONSÁVEL POR CONSULTAR O STATUS DE UM PAGAMENTO PIX
    // UTILIZA O ENDPOINT /orders/{id} DO PAGBANK
    // RETORNA O PagBankPaymentResponseDto COM AS INFORMAÇÕES ATUALIZADAS
    // -----------------------------------------------------------------------------
    public PagBankPaymentResponseDto getPaymentStatus(String orderId) {

        return pagBankClient.get()
                // ENDPOINT PARA CONSULTAR ORDEM PELO ID
                .uri("/orders/{id}", orderId)

                .retrieve()

                // -----------------------------------------------------------------------------
                // TRATAMENTO DE ERROS HTTP 4xx DURANTE CONSULTA DO STATUS
                // -----------------------------------------------------------------------------
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        response.bodyToMono(String.class)
                                .map(body ->
                                        new BadRequestException("Error fetching payment status: " + body)
                                )
                )

                // -----------------------------------------------------------------------------
                // TRATAMENTO DE ERROS HTTP 5xx DO PAGBANK DURANTE CONSULTA
                // -----------------------------------------------------------------------------
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        response.bodyToMono(String.class)
                                .map(body ->
                                        new BadRequestException("Internal PagBank server error while fetching status: " + body)
                                )
                )

                // -----------------------------------------------------------------------------
                // CONVERTE O JSON COM O STATUS ATUAL EM PagBankPaymentResponseDto
                // -----------------------------------------------------------------------------
                .bodyToMono(PagBankPaymentResponseDto.class)

                // -----------------------------------------------------------------------------
                // EXECUTA DE FORMA SINCRONA PARA COMPATIBILIDADE COM SUA ARQUITETURA
                // -----------------------------------------------------------------------------
                .block();
    }

}
