package br.com.educelulares.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

    //**
    //* -------------------------------------------------------------------------
    //* EXCEÇÃO DE RECURSO NÃO ENCONTRADO (NotFoundException)
    //* LANÇADA QUANDO UM REGISTRO (ORDER, PRODUCT, CATEGORY, PAYMENT, ETC.)
    //* NÃO É LOCALIZADO NA BASE DE DADOS OU NÃO EXISTE NA APLICAÇÃO.
    //* PRODUZ UMA RESPOSTA PADRÃO EM FORMATO ProblemDetail (HTTP 404).
    //* -------------------------------------------------------------------------
    //**
    public class NotFoundException extends BusinessException {

    // ---------------------------------------------------------------------
    // CONSTRUTOR QUE RECEBE A MENSAGEM DESCRITIVA DO ERRO
    // CHAMA O CONSTRUTOR DA SUPERCLASSE (RuntimeException)
    // ---------------------------------------------------------------------
    public NotFoundException(String message) {
        super(message);
    }

    // ---------------------------------------------------------------------
    // CONVERTE ESTA EXCEÇÃO EM UM OBJETO ProblemDetail
    // UTILIZADO PELO RestExceptionHandler PARA RETORNAR ERROS PADRONIZADOS
    // ---------------------------------------------------------------------
        @Override
    public ProblemDetail toProblemDetail() {
        // CRIA UM PROBLEMDETAIL COM STATUS 404 — NOT_FOUND
        var pb = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        // TÍTULO PADRÃO DO ERRO
        pb.setTitle("Recurso não encontrado.");

        // DETALHE DO ERRO COM A MENSAGEM ESPECÍFICA FORNECIDA AO LANÇAR A EXCEÇÃO
        pb.setDetail(this.getMessage());

        return pb;
    }
}
