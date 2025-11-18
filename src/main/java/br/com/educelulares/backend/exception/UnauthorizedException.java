package br.com.educelulares.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

/**
 * -------------------------------------------------------------------------
 * EXCEÇÃO DE AUTENTICAÇÃO (UnauthorizedException)
 * LANÇADA QUANDO O USUÁRIO NÃO ESTÁ AUTENTICADO OU AS CREDENCIAIS SÃO INVÁLIDAS.
 * RETORNA UM OBJETO ProblemDetail PADRÃO COM STATUS HTTP 401 (UNAUTHORIZED).
 * -------------------------------------------------------------------------
 */
public class UnauthorizedException extends BusinessException {

    // ---------------------------------------------------------------------
    // CONSTRUTOR QUE RECEBE A MENSAGEM DO ERRO DE AUTENTICAÇÃO
    // CHAMA O CONSTRUTOR DA SUPERCLASSE (RuntimeException)
    // ---------------------------------------------------------------------
    public UnauthorizedException(String message) {
        super(message);
    }

    // ---------------------------------------------------------------------
    // CONVERTE ESTA EXCEÇÃO EM UM ProblemDetail
    // ESTE MÉTODO SERÁ UTILIZADO PELO RestExceptionHandler AUTOMATICAMENTE
    // ---------------------------------------------------------------------
    @Override
    public ProblemDetail toProblemDetail() {

        // CRIA O OBJETO DE ERRO COM STATUS HTTP 401 (UNAUTHORIZED)
        var pb = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);

        // TÍTULO PADRÃO PARA ERROS DE AUTENTICAÇÃO
        pb.setTitle("Falha na autenticação.");

        // MENSAGEM DETALHADA DO ERRO
        pb.setDetail(this.getMessage());

        return pb;
    }
}






