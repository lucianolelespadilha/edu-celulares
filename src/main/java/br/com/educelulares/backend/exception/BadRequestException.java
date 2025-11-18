package br.com.educelulares.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class BadRequestException extends BusinessException {

    public BadRequestException(String message) {
        super(message);
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pb = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pb.setTitle("Requisição inválidad");
        pb.setDetail(this.getMessage());
        return pb;

    }
}
