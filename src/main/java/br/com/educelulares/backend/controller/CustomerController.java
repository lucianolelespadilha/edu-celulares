package br.com.educelulares.backend.controller;

import br.com.educelulares.backend.entity.Customer;
import br.com.educelulares.backend.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerRepository customerRepository;

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer){
        Customer savedCustomer = customerRepository.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers(){
        return ResponseEntity.ok(customerRepository.findAll());
    }

}
