package br.com.educelulares.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

// -----------------------------------------------------------------------------
// EXCEÇÃO BASE DE REGRA DE NEGÓCIO (BusinessException)
// ESSA CLASSE É ABSTRATA E SERVE DE MODELO PARA OUTRAS EXCEÇÕES DE NEGÓCIO
// TODAS AS EXCEÇÕES ESPECÍFICAS DEVEM IMPLEMENTAR O MÉTODO toProblemDetail()
// -----------------------------------------------------------------------------
public abstract class BusinessException extends RuntimeException {

    // -------------------------------------------------------------------------
    // CONSTRUTOR QUE RECEBE A MENSAGEM DA EXCEÇÃO
    // CHAMA O CONSTRUTOR DA CLASSE MÃE (RuntimeException)
    // -------------------------------------------------------------------------
    public BusinessException(String mensagem) {
        super(mensagem);
    }

    // -------------------------------------------------------------------------
    // MÉTODO ABSTRATO
    // CADA EXCEÇÃO FILHA SERÁ RESPONSÁVEL POR GERAR SEU PRÓPRIO ProblemDetail
    // (EX: NotFoundException retorna 404, ForbiddenException retorna 403, etc.)
    // -------------------------------------------------------------------------
    public abstract ProblemDetail toProblemDetail();
}