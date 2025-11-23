package br.com.educelulares.backend.controller;

import br.com.educelulares.backend.dto.PagBankOrderResponseDto;
import br.com.educelulares.backend.service.PagBankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pagBank")
@RequiredArgsConstructor
public class PagBankController {


    private final PagBankService pagBankService;

    @PostMapping("/pix/{orderId}")
    public ResponseEntity<PagBankOrderResponseDto> createPaymentPix(@PathVariable Long orderId){

        PagBankOrderResponseDto response = pagBankService.createPaymentPix(orderId);
        return ResponseEntity.ok(response);
    }

}