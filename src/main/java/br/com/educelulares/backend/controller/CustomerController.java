package br.com.educelulares.backend.controller;

import br.com.educelulares.backend.entity.Customer;
import br.com.educelulares.backend.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// -------------------------------------------------------------------------
// CONTROLADOR RESPONSÁVEL PELO GERENCIAMENTO DE CLIENTES (CUSTOMERS)
// PERMITE CRIAR, LISTAR, CONSULTAR, ATUALIZAR E EXCLUIR CLIENTES
// -------------------------------------------------------------------------
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    // INJEÇÃO AUTOMÁTICA DO REPOSITÓRIO DE CLIENTES
    private final CustomerRepository customerRepository;

    // ---------------------------------------------------------------------
    // CRIA UM NOVO CLIENTE
    // ---------------------------------------------------------------------
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        // SALVA O CLIENTE NO BANCO
        Customer savedCustomer = customerRepository.save(customer);

        // RETORNA HTTP 201 (CREATED) COM O CLIENTE SALVO
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
    }

    // ---------------------------------------------------------------------
    // RETORNA TODOS OS CLIENTES
    // ---------------------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        // RETORNA HTTP 200 (OK) COM A LISTA DE CLIENTES CADASTRADOS
        return ResponseEntity.ok(customerRepository.findAll());
    }

    // ---------------------------------------------------------------------
    // RETORNA UM CLIENTE PELO ID
    // ---------------------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        // PROCURA O CLIENTE PELO ID INFORMADO
        return customerRepository.findById(id)
                // RETORNA 200 SE ENCONTRAR OU 404 SE NÃO EXISTIR
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ---------------------------------------------------------------------
    // ATUALIZA UM CLIENTE EXISTENTE
    // ---------------------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer updatedCustomer) {

        // VERIFICA SE O CLIENTE EXISTE NO BANCO
        return customerRepository.findById(id)
                .map(existingCustomer -> {
                    // ATUALIZA OS CAMPOS PERMITIDOS
                    existingCustomer.setName(updatedCustomer.getName());
                    existingCustomer.setEmail(updatedCustomer.getEmail());

                    // SALVA AS ALTERAÇÕES
                    Customer savedCustomer = customerRepository.save(existingCustomer);

                    // RETORNA 200 (OK) COM O CLIENTE ATUALIZADO
                    return ResponseEntity.ok(savedCustomer);
                })
                // SE NÃO EXISTIR, RETORNA 404
                .orElse(ResponseEntity.notFound().build());
    }

    // ---------------------------------------------------------------------
    // DELETA UM CLIENTE PELO ID
    // ---------------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        // VERIFICA SE O CLIENTE EXISTE
        if (!customerRepository.existsById(id)) {
            // SE NÃO EXISTIR, RETORNA 404
            return ResponseEntity.notFound().build();
        }

        // REMOVE O CLIENTE DO BANCO
        customerRepository.deleteById(id);

        // RETORNA 204 (NO CONTENT) INDICANDO SUCESSO
        return ResponseEntity.noContent().build();
    }
}
