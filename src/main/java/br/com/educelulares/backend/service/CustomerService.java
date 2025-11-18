package br.com.educelulares.backend.service;

import br.com.educelulares.backend.dto.CustomerCreateDto;
import br.com.educelulares.backend.dto.CustomerDto;
import br.com.educelulares.backend.entity.Customer;
import br.com.educelulares.backend.exception.NotFoundException;
import br.com.educelulares.backend.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// -------------------------------------------------------------------------
// CAMADA DE SERVIÇO RESPONSÁVEL PELA REGRA DE NEGÓCIO DE CLIENTES (CUSTOMERS)
// INTERMEDIA A COMUNICAÇÃO ENTRE CONTROLLER E REPOSITÓRIO
// -------------------------------------------------------------------------
@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    // ---------------------------------------------------------------------
    // CRIA UM NOVO CLIENTE A PARTIR DE UM DTO DE ENTRADA
    // ---------------------------------------------------------------------
    public CustomerDto createCustomer(CustomerCreateDto customerCreateDto) {
        // CONVERTE O DTO EM ENTIDADE (Customer)
        Customer customer = new Customer();
        customer.setName(customerCreateDto.name());
        customer.setEmail(customerCreateDto.email());

        // SALVA O CLIENTE NO BANCO DE DADOS
        Customer saved = customerRepository.save(customer);

        // RETORNA UM DTO DE SAÍDA COM OS DADOS PERSISTIDOS
        return new CustomerDto(saved.getId(), saved.getName(), saved.getEmail());
    }
    // ---------------------------------------------------------------------
    // LISTA TODOS OS CLIENTES CADASTRADOS
    // ---------------------------------------------------------------------
    public List<CustomerDto> findAll() {
        return customerRepository.findAll().stream()
                .map(c-> new CustomerDto(c.getId(),c.getName(),c.getEmail()))
                .collect(Collectors.toList());
    }
    // ---------------------------------------------------------------------
    // BUSCA UM CLIENTE ESPECÍFICO PELO ID
    // ---------------------------------------------------------------------
    public CustomerDto findById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Customer not found"));
        return new CustomerDto(customer.getId(), customer.getName(), customer.getEmail());
    }

    // ---------------------------------------------------------------------
    // ATUALIZA OS DADOS DE UM CLIENTE EXISTENTE
    // ---------------------------------------------------------------------
    public CustomerDto updateCustomer(Long id ,CustomerCreateDto customerCreateDto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Customer not found"));
        customer.setName(customerCreateDto.name());
        customer.setEmail(customerCreateDto.email());
        Customer updatedCustomer = customerRepository.save(customer);
        return new CustomerDto(updatedCustomer.getId(), updatedCustomer.getName(), updatedCustomer.getEmail());
    }

    // ---------------------------------------------------------------------
    // EXCLUI UM CLIENTE PELO ID
    // ---------------------------------------------------------------------
    public void deleteCustomer(Long id) {
        if(!customerRepository.existsById(id)){
            throw new NotFoundException("Customer not found");
        }
        customerRepository.deleteById(id);
    }

}
