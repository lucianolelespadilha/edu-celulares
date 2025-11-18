package br.com.educelulares.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

/**
 * -------------------------------------------------------------------------
 * EXCEÇÃO DE ACESSO NEGADO (ForbiddenException)
 * UTILIZADA QUANDO O CLIENTE TENTA EXECUTAR UMA AÇÃO QUE NÃO TEM PERMISSÃO.
 * RETORNA UM ProblemDetail PADRONIZADO COM STATUS HTTP 403 (FORBIDDEN).
 * -------------------------------------------------------------------------
 */
public class ForbiddenException extends BusinessException {

    // ---------------------------------------------------------------------
    // CONSTRUTOR QUE RECEBE A MENSAGEM ESPECÍFICA DO ERRO
    // CHAMA O CONSTRUTOR DA SUPERCLASSE (RuntimeException)
    // ---------------------------------------------------------------------
    public ForbiddenException(String message) {
        super(message);
    }

    // ---------------------------------------------------------------------
    // CONVERTE ESTA EXCEÇÃO EM UM ProblemDetail
    // UTILIZADO PELO RestExceptionHandler PARA UNIFICAR RESPOSTAS DE ERRO
    // ---------------------------------------------------------------------
    @Override
    public ProblemDetail toProblemDetail() {

        // GERA UM PROBLEMDETAIL COM STATUS 403 — FORBIDDEN
        var pb = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);

        // TÍTULO PADRÃO PARA ERROS DE PERMISSÃO
        pb.setTitle("Acesso negado.");

        // DETALHE DO ERRO COM A MENSAGEM ESPECÍFICA
        pb.setDetail(this.getMessage());

        return pb;
    }
}
