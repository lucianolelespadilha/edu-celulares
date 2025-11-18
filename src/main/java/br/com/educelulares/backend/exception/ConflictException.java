package br.com.educelulares.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

/**
 * -------------------------------------------------------------------------
 * EXCEÇÃO DE CONFLITO (ConflictException)
 * UTILIZADA QUANDO A OPERAÇÃO NÃO PODE SER EXECUTADA POR UM CONFLITO DE ESTADO.
 * EXEMPLOS: recurso já existente, operação duplicada, conflito de versão etc.
 * RETORNA UM ProblemDetail COM STATUS HTTP 409 (CONFLICT).
 * -------------------------------------------------------------------------
 */
public class ConflictException extends BusinessException {

    // ---------------------------------------------------------------------
    // CONSTRUTOR QUE RECEBE A MENSAGEM DO CONFLITO
    // CHAMA O CONSTRUTOR PADRÃO DE RuntimeException
    // ---------------------------------------------------------------------
    public ConflictException(String message) {
        super(message);
    }

    // ---------------------------------------------------------------------
    // CONVERTE A EXCEÇÃO EM UM OBJETO ProblemDetail
    // ESTE OBJETO SERÁ RETORNADO PELO HANDLER GLOBAL (RestExceptionHandler)
    // ---------------------------------------------------------------------
    @Override
    public ProblemDetail toProblemDetail() {

        // CRIA UM PROBLEMDETAIL COM STATUS HTTP 409 (CONFLICT)
        var pb = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        // TÍTULO PADRÃO PARA ERROS DE CONFLITO
        pb.setTitle("Conflito ao processar a requisição.");

        // MENSAGEM DETALHADA COM A CAUSA DO ERRO
        pb.setDetail(this.getMessage());

        return pb;
    }
}



