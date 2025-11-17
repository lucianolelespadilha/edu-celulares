package br.com.educelulares.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// -----------------------------------------------------------------------------
// MANIPULADOR GLOBAL DE EXCEÇÕES DA APLICAÇÃO
// QUALQUER EXCEÇÃO LANÇADA PELOS CONTROLLERS OU SERVICES PASSA POR AQUI
// PERMITE RETORNAR ERROS PADRONIZADOS EM FORMATO ProblemDetail (RFC 7807)
// -----------------------------------------------------------------------------
@RestControllerAdvice
public class RestExceptionHandler {

    // -------------------------------------------------------------------------
    // TRATA EXCEÇÕES DE REGRA DE NEGÓCIO (BusinessException)
    // UTILIZADO QUANDO ALGUMA REGRA DA APLICAÇÃO É VIOLADA
    // RETORNA O PROBLEMDETAIL DEFINIDO NA PRÓPRIA EXCEÇÃO
    // -------------------------------------------------------------------------
    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handleBusinessException(BusinessException exception) {
        return exception.toProblemDetail();
    }

    // -------------------------------------------------------------------------
    // TRATA ERROS DE VALIDAÇÃO (@Valid)
    // OCORRE QUANDO CAMPOS OBRIGATÓRIOS OU FORMATOS ESTÃO INVÁLIDOS NO DTO
    // RETORNA UMA LISTA DE CAMPOS QUE ESTÃO INCORRETOS
    // -------------------------------------------------------------------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

        // LISTA DE CAMPOS INVÁLIDOS + MENSAGENS DE ERRO
        var fieldErrors = exception.getFieldErrors().stream()
                .map(e -> new InvalidParam(
                        e.getField(),          // nome do campo inválido
                        e.getDefaultMessage()  // mensagem padrão da validação
                ))
                .toList();

        // GERA O OBJETO PADRÃO DE ERRO HTTP 400 (BAD_REQUEST)
        var pb = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        // TÍTULO DO PROBLEMA
        pb.setTitle("Os parâmetros da requisição são inválidos.");

        // ADICIONA A LISTA DE PARAMETROS INVÁLIDOS NO CORPO DO PROBLEMA
        pb.setProperty("invalid-params", fieldErrors);

        return pb;
    }

    // -------------------------------------------------------------------------
    // RECORD APENAS PARA ESTRUTURAR OS CAMPOS INVÁLIDOS RETORNADOS NAS VALIDAÇÕES
    // CONTÉM O NOME DO CAMPO E O MOTIVO DO ERRO
    // -------------------------------------------------------------------------
    private record InvalidParam(
            String name,
            String reason
    ) {}
}