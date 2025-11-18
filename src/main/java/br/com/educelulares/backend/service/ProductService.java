package br.com.educelulares.backend.service;

import br.com.educelulares.backend.dto.ProductCreateDto;
import br.com.educelulares.backend.dto.ProductDto;
import br.com.educelulares.backend.entity.Product;
import br.com.educelulares.backend.exception.NotFoundException;
import br.com.educelulares.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// -----------------------------------------------------------------------------
// CAMADA DE SERVIÇO RESPONSÁVEL POR GERENCIAR PRODUTOS
// REALIZA OPERAÇÕES DE CRIAÇÃO, CONSULTA, ATUALIZAÇÃO E EXCLUSÃO
// -----------------------------------------------------------------------------
@Service
@RequiredArgsConstructor
public class ProductService {

    // INJEÇÃO AUTOMÁTICA DO REPOSITÓRIO
    private final ProductRepository productRepository;

    // -------------------------------------------------------------------------
    // CRIA UM NOVO PRODUTO
    // -------------------------------------------------------------------------
    public ProductDto createdProduct(ProductCreateDto productCreateDto) {

        // VALIDAÇÃO DO NOME DO PRODUTO
        if (productCreateDto.name() == null || productCreateDto.name().isBlank()) {
            throw new NotFoundException("PRODUCT NAME IS REQUIRED");
        }

        // VALIDAÇÃO DO PREÇO DO PRODUTO
        if (productCreateDto.price() == null) {
            throw new NotFoundException("PRICE IS REQUIRED");
        }

        // CRIA UMA NOVA INSTÂNCIA DE PRODUCT
        Product product = new Product();
        product.setName(productCreateDto.name());
        product.setDescription(productCreateDto.description());
        product.setPrice(productCreateDto.price());

        // SALVA NO BANCO DE DADOS
        Product savedProduct = productRepository.save(product);

        // RETORNA O DTO
        return toProductDto(savedProduct);
    }

    // -------------------------------------------------------------------------
    // RETORNA TODOS OS PRODUTOS
    // -------------------------------------------------------------------------
    public List<ProductDto> findAllProducts() {
        return productRepository.findAll().stream()
                .map(this::toProductDto)
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------------------------
    // RETORNA UM PRODUTO PELO ID
    // -------------------------------------------------------------------------
    public ProductDto findProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("PRODUCT NOT FOUND WITH ID: " + id));

        return toProductDto(product);
    }

    // -------------------------------------------------------------------------
    // ATUALIZA UM PRODUTO EXISTENTE
    // -------------------------------------------------------------------------
    public ProductDto updateProduct(ProductCreateDto productCreateDto, Long id) {

        // BUSCA O PRODUTO QUE SERÁ ATUALIZADO
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("PRODUCT NOT FOUND WITH ID: " + id));

        // ATUALIZA CAMPOS
        product.setName(productCreateDto.name());
        product.setDescription(productCreateDto.description());
        product.setPrice(productCreateDto.price());

        // SALVA NO BANCO
        Product updatedProduct = productRepository.save(product);

        return toProductDto(updatedProduct);
    }

    // -------------------------------------------------------------------------
    // DELETA UM PRODUTO PELO ID
    // -------------------------------------------------------------------------
    public void deleteProductById(Long id) {

        if (!productRepository.existsById(id)) {
            throw new NotFoundException("PRODUCT NOT FOUND WITH ID: " + id);
        }

        productRepository.deleteById(id);
    }

    // -------------------------------------------------------------------------
    // CONVERSÃO DE ENTITY PARA DTO
    // -------------------------------------------------------------------------
    private ProductDto toProductDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription()
        );
    }
}
