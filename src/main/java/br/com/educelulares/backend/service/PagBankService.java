package br.com.educelulares.backend.service;

import br.com.educelulares.backend.dto.*;
import br.com.educelulares.backend.entity.Order;
import br.com.educelulares.backend.exception.NotFoundException;
import br.com.educelulares.backend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagBankService {

    // -------------------------------------------------------------------------
    // REPOSITÓRIO RESPONSÁVEL POR CONSULTAR ORDENS NO BANCO DE DADOS
    // -------------------------------------------------------------------------
    private final OrderRepository orderRepository;

    // -------------------------------------------------------------------------
    // WEBCLIENT CONFIGURADO PARA SE COMUNICAR COM A API DO PAGBANK
    // (COM HEADERS E AUTENTICAÇÃO DEFINIDOS NA CONFIGURAÇÃO DO PROJETO)
    // -------------------------------------------------------------------------
    private final WebClient pagBankClient;


    // -------------------------------------------------------------------------
    // MÉTODO RESPONSÁVEL POR CRIAR UM PAGAMENTO VIA PIX NO PAGBANK
    // COM BASE NO ID DE UMA ORDEM LOCAL
    // -------------------------------------------------------------------------
    public PagBankOrderResponseDto createPaymentPix(Long orderId) {

        // ---------------------------------------------------------------------
        // BUSCA A ORDEM NO BANCO DE DADOS
        // SE NÃO EXISTIR → LANÇA ERRO 404
        // ---------------------------------------------------------------------
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found " + orderId));

        // ---------------------------------------------------------------------
        // CONVERTE OS PRODUTOS DO PEDIDO PARA O FORMATO ACEITO PELO PAGBANK
        // CADA ITEM DO CARRINHO É TRANSFORMADO EM UM PagBankItemDto
        // ---------------------------------------------------------------------
        List<PagBankItemDto> items = order.getItems().stream()
                .map(item -> new PagBankItemDto(
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getPrice().intValue()
                ))
                .toList();

        // ---------------------------------------------------------------------
        // CALCULA O VALOR TOTAL DO PEDIDO, UMA VEZ QUE A ENTIDADE Order
        // NÃO POSSUI O MÉTODO getTotalAmount()
        //
        // SOMA: price * quantity PARA CADA ITEM
        // RESULTADO CONVERTIDO PARA INT (CENTAVOS)
        // ---------------------------------------------------------------------
        int orderTotalAmount = order.getItems().stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .intValue();

        // ---------------------------------------------------------------------
        // DADOS DO CLIENTE EM FORMATO EXIGIDO PELO PAGBANK
        // ATUALMENTE MOCKADOS — SERÃO SUBSTITUÍDOS POR DADOS REAIS
        // QUANDO O CADASTRO DE CLIENTES ESTIVER IMPLEMENTADO
        // ---------------------------------------------------------------------
        PagBankCustomerDto customer = new PagBankCustomerDto(
                "Test Client",
                "email@test.com",
                new PagBankPhoneDto("55", "11", "999999999")
        );

        // ---------------------------------------------------------------------
        // ENDEREÇO DO CLIENTE NO MODELO PADRÃO DO PAGBANK
        // ATUALMENTE FIXO PARA TESTES
        // ---------------------------------------------------------------------
        PagBankAddressDto address = new PagBankAddressDto(
                "Test Street",
                "123",
                "House",
                "District",
                "São Paulo",
                "SP",
                "SP",
                "BRA",
                "01001000"
        );

        // ---------------------------------------------------------------------
        // OBJETO DE ENVIO (SHIPPING) EXIGIDO PELO PAGBANK
        // CONTÉM: ENDEREÇO + VALOR DO FRETE (AQUI → VALOR TOTAL DO PEDIDO)
        // ---------------------------------------------------------------------
        PagBankShippingDto shipping = new PagBankShippingDto(
                address,
                orderTotalAmount
        );

        // ---------------------------------------------------------------------
        // URL DO WEBHOOK QUE RECEBERÁ AS NOTIFICAÇÕES DO PAGBANK
        // EM AMBIENTE DE PRODUÇÃO RECEBERÁ SUA URL REAL
        // ---------------------------------------------------------------------
        List<String> notificationUrls = List.of("https://seusite.com/webhook/pagbank");

        // ---------------------------------------------------------------------
        // OBJETO FINAL DE REQUISIÇÃO ENVIADO PARA O PAGBANK
        // CONTÉM: ORDER_ID, CLIENTE, ITENS, SHIPPING E WEBHOOKS
        // ---------------------------------------------------------------------
        PagBankOrderRequestDto requestDto = new PagBankOrderRequestDto(
                order.getId().toString(),
                customer,
                items,
                shipping,
                notificationUrls
        );

        // ---------------------------------------------------------------------
        // CHAMADA HTTP PARA GERAR A ORDEM DE PAGAMENTO NO PAGBANK
        // MÉTODO .block() UTILIZADO POIS O FLUXO DEVE SER SINCRONO
        // ---------------------------------------------------------------------
        PagBankOrderResponseDto responseDto = pagBankClient
                .post()
                .uri("/orders")
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(PagBankOrderResponseDto.class)
                .block();

        // ---------------------------------------------------------------------
        // RETORNA A RESPOSTA COMPLETA DO PAGBANK PARA A CONTROLLER
        // ---------------------------------------------------------------------
        return responseDto;
    }
}
