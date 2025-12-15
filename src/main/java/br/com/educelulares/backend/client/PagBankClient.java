package br.com.educelulares.backend.client;

import br.com.educelulares.backend.dto.pagbank.PagBankOrderRequestDto;
import br.com.educelulares.backend.dto.pagbank.PagBankOrderResponseDto;
import br.com.educelulares.backend.exception.BadRequestException;
import br.com.educelulares.backend.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class PagBankClient {

    /** WebClient configurado com BEARER TOKEN (PagBankConfig) */
    private final WebClient pagBankWebClient;

    // =====================================================================
    // 1) CRIAR PEDIDO PIX (QR CODE)
    // =====================================================================
    public PagBankOrderResponseDto createPixOrder(PagBankOrderRequestDto request) {

        log.info("[PAGBANK][CREATE ORDER] Enviando pedido reference_id={}",
                request.getReferenceId());

        return pagBankWebClient
                .post()
                .uri("/orders")
                .bodyValue(request)
                .retrieve()

                // ----------------- TRATAMENTO 4xx -------------------------
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        response.bodyToMono(String.class).map(body -> {
                            int code = response.statusCode().value();

                            if (code == 401) {
                                log.error("[PAGBANK] Unauthorized (401) → {}", body);
                                return new UnauthorizedException("Unauthorized: " + body);
                            }

                            log.error("[PAGBANK] Erro 4xx → {}", body);
                            return new BadRequestException("Erro PagBank 4xx: " + body);
                        })
                )

                // ----------------- TRATAMENTO 5xx -------------------------
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        response.bodyToMono(String.class).map(body -> {
                            log.error("[PAGBANK] Erro 5xx → {}", body);
                            return new BadRequestException("Erro PagBank 5xx: " + body);
                        })
                )

                // ----------------- SUCESSO -------------------------------
                .bodyToMono(PagBankOrderResponseDto.class)
                .doOnNext(resp -> {
                    log.info("[PAGBANK][ORDER CREATED] id={} status={}",
                            resp.getId(), resp.getStatus());

                    if (resp.getQrCodes() != null && !resp.getQrCodes().isEmpty()) {
                        log.info("[PAGBANK][QR TEXT] {}", resp.getQrCodes().get(0).getText());
                    }
                })

                .block();
    }

    // =====================================================================
// 2) CONSULTAR STATUS DE UM PEDIDO PIX NO PAGBANK
//
// ESTE MÉTODO REALIZA:
//   - GET /orders/{id}
//   - TRATAMENTO COMPLETO DE ERROS 4xx E 5xx
//   - LOGS DE DEPURAÇÃO E STATUS ATUAL DO PEDIDO
//
// RETORNA:
//   PagBankOrderResponseDto COM O STATUS ATUAL DO PAGAMENTO PIX
//
// IMPORTANTE:
//   ESTE MÉTODO NÃO ALTERA O ESTADO DO PEDIDO NO BANCO LOCAL.
//   APENAS CONSULTA O PAGBANK E DEVOLVE O RESULTADO.
// =====================================================================
    public PagBankOrderResponseDto getOrderStatus(String orderId) {

        log.info("[PAGBANK][STATUS] INICIANDO CONSULTA DO PEDIDO id={}", orderId);

        return pagBankWebClient
                .get()
                // =========================================================
                // ENDPOINT OFICIAL PARA CONSULTAR UMA ORDEM PIX
                // =========================================================
                .uri("/orders/{id}", orderId)

                .retrieve()

                // =========================================================
                // TRATAMENTO DE ERROS 4xx (ERROS DO CLIENTE)
                //
                // EXEMPLOS:
                //   - 400 → REQUEST INVÁLIDO
                //   - 401 → TOKEN INVÁLIDO / EXPIRADO
                //   - 404 → PEDIDO NÃO ENCONTRADO
                // =========================================================
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        response.bodyToMono(String.class).map(body -> {
                            log.error("[PAGBANK][ERRO 4xx] FALHA AO CONSULTAR STATUS: {}", body);
                            return new BadRequestException("Erro ao consultar pedido PIX: " + body);
                        })
                )

                // =========================================================
                // TRATAMENTO DE ERROS 5xx (ERROS NO SERVIDOR PAGBANK)
                // =========================================================
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        response.bodyToMono(String.class).map(body -> {
                            log.error("[PAGBANK][ERRO 5xx] SERVIDOR PAGBANK FALHOU: {}", body);
                            return new BadRequestException("Erro interno do PagBank: " + body);
                        })
                )

                // =========================================================
                // SUCESSO → DESSERIALIZA O JSON E DEVOLVE O DTO
                // =========================================================
                .bodyToMono(PagBankOrderResponseDto.class)

                // LOGA STATUS RECEBIDO
                .doOnNext(resp -> log.info(
                        "[PAGBANK][STATUS] RESPOSTA → pagbank_id={} status={}",
                        resp.getId(), resp.getStatus()
                ))

                // EXECUÇÃO BLOQUEANTE (PROJETO NÃO É REATIVO)
                .block();
    }

}
