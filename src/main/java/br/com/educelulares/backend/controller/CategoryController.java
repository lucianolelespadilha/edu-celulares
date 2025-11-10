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

// -------------------------------------------------------------------------
// CONTROLADOR RESPONSÁVEL PELO GERENCIAMENTO DE CATEGORIAS (CATEGORIES)
// PERMITE CRIAR, LISTAR, CONSULTAR, ATUALIZAR E EXCLUIR CATEGORIAS
// -------------------------------------------------------------------------
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    // INJEÇÃO AUTOMÁTICA DO REPOSITÓRIO DE CATEGORIAS
    private final CategoryRepository repository;

    // ---------------------------------------------------------------------
    // LISTA TODAS AS CATEGORIAS
    // ---------------------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        // BUSCA TODAS AS CATEGORIAS CADASTRADAS NO BANCO
        List<Category> categories = repository.findAll();

        // RETORNA HTTP 200 (OK) COM A LISTA DE CATEGORIAS
        return ResponseEntity.ok(categories);
    }

    // ---------------------------------------------------------------------
    // BUSCA UMA CATEGORIA PELO ID
    // ---------------------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable Long id) {
        // PROCURA A CATEGORIA PELO ID INFORMADO
        Optional<Category> category = repository.findById(id);

        // RETORNA 200 SE ENCONTRAR OU 404 SE NÃO EXISTIR
        return category.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ---------------------------------------------------------------------
    // CADASTRA UMA NOVA CATEGORIA
    // ---------------------------------------------------------------------
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Category> create(@RequestBody Category category) {
        // SALVA A NOVA CATEGORIA NO BANCO
        Category saved = repository.save(category);

        // RETORNA HTTP 201 (CREATED) COM A CATEGORIA SALVA
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ---------------------------------------------------------------------
    // ATUALIZA UMA CATEGORIA EXISTENTE PELO ID
    // ---------------------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable Long id, @RequestBody Category category) {
        // VERIFICA SE A CATEGORIA EXISTE NO BANCO
        if (!repository.existsById(id)) {
            // SE NÃO EXISTIR, RETORNA 404
            return ResponseEntity.notFound().build();
        }

        // DEFINE O ID PARA GARANTIR QUE SERÁ ATUALIZADA A CATEGORIA CORRETA
        category.setId(id);

        // SALVA AS ALTERAÇÕES NO BANCO
        Category updated = repository.save(category);

        // RETORNA 200 (OK) COM A CATEGORIA ATUALIZADA
        return ResponseEntity.ok(updated);
    }

    // ---------------------------------------------------------------------
    // DELETA UMA CATEGORIA PELO ID
    // ---------------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        // VERIFICA SE A CATEGORIA EXISTE
        if (!repository.existsById(id)) {
            // SE NÃO EXISTIR, RETORNA 404
            return ResponseEntity.notFound().build();
        }

        // REMOVE A CATEGORIA DO BANCO
        repository.deleteById(id);

        // RETORNA 204 (NO CONTENT) INDICANDO SUCESSO NA EXCLUSÃO
        return ResponseEntity.noContent().build();
    }
}
