package br.com.educelulares.backend.controller;

import br.com.educelulares.backend.entity.Category;
import br.com.educelulares.backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository repository;


    //LISTAR TOTAS AS CATEGORIAS
    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        List<Category>categories = repository.findAll();
        return ResponseEntity.ok(categories);
    }
    //BUSCAR CATEGORIA POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable Long id) {

        Optional<Category> category = repository.findById(id);
        return category.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    //CADASTRAR UMA CATEGORIA
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Category> creat(@RequestBody Category category) {
        Category saved = repository.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    //ATUALIZAR CATEGORIA POR ID
    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable Long id, @RequestBody Category category) {
        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        category.setId(id);
        Category updated = repository.save(category);
        return ResponseEntity.ok(updated);
    }
    //DELETAR CATEGORIA POR ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }



}
