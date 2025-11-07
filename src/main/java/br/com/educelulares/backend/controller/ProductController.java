package br.com.educelulares.backend.controller;


import br.com.educelulares.backend.entity.Category;
import br.com.educelulares.backend.entity.Product;
import br.com.educelulares.backend.repository.CategoryRepository;
import br.com.educelulares.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;


    //RETORNA A LISTA DE PRODUTOS
    @GetMapping
    public ResponseEntity<List<Product>> findAll(){
        return ResponseEntity.ok(repository.findAll());
    }

    //BUSCAR PRODUTO POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //CADASTRAR UM PRODUTO
    @PostMapping
    public ResponseEntity<Product> creat(@RequestBody Product product){
        if(product.getCategory() == null || product.getCategory().getId() == null){
            return ResponseEntity.badRequest().build();
        }
        Category category = categoryRepository.findById(product.getCategory()
                .getId()).orElseThrow(()->  new RuntimeException("Category not found"));
        product.setCategory(category);
        Product saved = repository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    //ATUALIZAR UM PRODUTO
    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product newData){
       return repository.findById(id)
               .map(product -> {
                   product.setName(newData.getName());
                   product.setPrice(newData.getPrice());
                   product.setDescription(newData.getDescription());

                   if (newData.getCategory() != null) {
                       Category category = categoryRepository.findById(newData.getCategory().getId())
                               .orElseThrow(() -> new  RuntimeException("Category not found"));
                       product.setCategory(category);
                   }
                   return ResponseEntity.ok(repository.save(product));
               })
               .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //DELETAR UM PRODUTO
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
