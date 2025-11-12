package br.com.educelulares.backend.controller;

import br.com.educelulares.backend.dto.CustomerCreateDto;
import br.com.educelulares.backend.dto.CustomerDto;
import br.com.educelulares.backend.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//-------------------------------------------------------------------------
// CONTROLADOR RESPONSÁVEL PELO GERENCIAMENTO DE CLIENTES (CUSTOMERS)
// PERMITE CRIAR, LISTAR, CONSULTAR, ATUALIZAR E EXCLUIR CLIENTES
// -------------------------------------------------------------------------
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    // INJEÇÃO AUTOMÁTICA DO SERVIÇO DE CLIENTES
    private final CustomerService customerService;

    // ---------------------------------------------------------------------
    // CRIA UM NOVO CLIENTE
    // ---------------------------------------------------------------------
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerCreateDto customerCreateDto) {
        // CHAMA O SERVIÇO PARA CRIAR UM NOVO CLIENTE E RETORNA STATUS 201 (CREATED)
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(customerService.createCustomer(customerCreateDto));
    }

    // ---------------------------------------------------------------------
    // LISTA TODOS OS CLIENTES CADASTRADOS
    // ---------------------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<CustomerDto>> findAll() {
        // RETORNA STATUS 200 (OK) COM A LISTA COMPLETA DE CLIENTES
        return ResponseEntity.ok(customerService.findAll());
    }

    // ---------------------------------------------------------------------
    // BUSCA UM CLIENTE PELO ID
    // ---------------------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> findById(@PathVariable Long id) {
        // PROCURA O CLIENTE PELO ID INFORMADO E RETORNA STATUS 200 (OK)
        return ResponseEntity.ok(customerService.findById(id));
    }

    // ---------------------------------------------------------------------
    // ATUALIZA UM CLIENTE EXISTENTE PELO ID
    // ---------------------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(
            @PathVariable Long id,
            @RequestBody CustomerCreateDto customerCreateDto) {

        // CHAMA O SERVIÇO PARA ATUALIZAR E RETORNA STATUS 200 (OK)
        return ResponseEntity.ok(customerService.updateCustomer(id, customerCreateDto));
    }

    // ---------------------------------------------------------------------
    // DELETA UM CLIENTE PELO ID
    // ---------------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        // REMOVE O CLIENTE DO BANCO E RETORNA STATUS 204 (NO CONTENT)
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}