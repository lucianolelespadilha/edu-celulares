package br.com.educelulares.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

// -----------------------------------------------------------------------------
// EXCEÇÃO DE REGRA DE NEGÓCIO (BusinessException)
// LANÇADA QUANDO ALGUMA REGRA DA APLICAÇÃO É VIOLADA
// ESSA EXCEÇÃO PRODUZ UMA RESPOSTA PADRÃO EM FORMATO ProblemDetail (RFC 7807)
// -----------------------------------------------------------------------------
public class BusinessException extends RuntimeException {

    // -------------------------------------------------------------------------
    // CONSTRUTOR QUE RECEBE A MENSAGEM DA EXCEÇÃO
    // CHAMA O CONSTRUTOR DA CLASSE MÃE (RuntimeException)
    // -------------------------------------------------------------------------
    public BusinessException(String mensagem) {
        super(mensagem);
    }

    // -------------------------------------------------------------------------
    // CONVERTE A EXCEÇÃO PARA UM OBJETO ProblemDetail
    // ESSE FORMATO É PADRÃO PARA ERROS EM APIs REST (HTTP PROBLEM DETAILS)
    // -------------------------------------------------------------------------
    public ProblemDetail toProblemDetail() {

        // CRIA UM ERRO COM STATUS 400 — BAD_REQUEST
        // UTILIZADO QUANDO A REGRA DE NEGÓCIO FOI VIOLADA
        var pb = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        // TÍTULO PADRÃO DO ERRO DA APLICAÇÃO
        pb.setTitle("EduCelulares business error.");

        // DETALHE DO ERRO COM A MENSAGEM ESPECÍFICA
        pb.setDetail(this.getMessage());

        return pb;
    }
}