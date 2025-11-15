package br.com.educelulares.backend.controller;


import br.com.educelulares.backend.dto.ProductCreateDto;
import br.com.educelulares.backend.dto.ProductDto;
import br.com.educelulares.backend.service.OrderItemService;
import br.com.educelulares.backend.service.OrderService;
import br.com.educelulares.backend.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    // -------------------------------------------------------------------------
    // INJEÇÃO DOS SERVIÇOS NECESSÁRIOS PARA OPERAR PRODUTOS
    // -------------------------------------------------------------------------
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final ProductService productService;


    // -------------------------------------------------------------------------
    // CRIA UM NOVO PRODUTO
    // -------------------------------------------------------------------------
    @PostMapping
    @Transactional
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductCreateDto productCreateDto) {
        // CRIA O PRODUTO BASEADO NO DTO RECEBIDO
        ProductDto created = productService.createdProduct(productCreateDto);

        // RETORNA HTTP 201 (CREATED) COM O PRODUTO GERADO
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    // -------------------------------------------------------------------------
    // RETORNA TODOS OS PRODUTOS
    // -------------------------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<ProductDto>> findAllProducts() {
        // RETORNA HTTP 200 (OK) COM A LISTA COMPLETA DE PRODUTOS
        return ResponseEntity.ok(productService.findAllProducts());
    }


    // -------------------------------------------------------------------------
    // RETORNA UM PRODUTO PELO ID
    // -------------------------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable Long id) {
        // RETORNA 200 SE ENCONTRAR O PRODUTO OU EXCEÇÃO 404 SE NÃO EXISTIR
        return ResponseEntity.ok(productService.findProductById(id));
    }


    // -------------------------------------------------------------------------
    // ATUALIZA UM PRODUTO EXISTENTE PELO ID
    // -------------------------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id,
                                                    @RequestBody ProductCreateDto productCreateDto) {
        // ATUALIZA O PRODUTO COM OS NOVOS DADOS
        ProductDto updated = productService.updateProduct(productCreateDto, id);

        // RETORNA HTTP 200 COM O PRODUTO ATUALIZADO
        return ResponseEntity.ok(updated);
    }


    // -------------------------------------------------------------------------
    // DELETA UM PRODUTO PELO ID
    // -------------------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        // REMOVE O PRODUTO DO BANCO DE DADOS
        productService.deleteProductById(id);

        // RETORNA HTTP 204 (NO CONTENT) SEM CORPO
        return ResponseEntity.noContent().build();
    }
}
