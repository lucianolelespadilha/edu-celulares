package br.com.educelulares.backend.controller;

import br.com.educelulares.backend.dto.CategoryCreateDto;
import br.com.educelulares.backend.dto.CategoryDto;
import br.com.educelulares.backend.entity.Category;
import br.com.educelulares.backend.repository.CategoryRepository;
import br.com.educelulares.backend.service.CategoryService;
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

    // INJEÇÃO AUTOMÁTICA DO SERVICE DE CATEGORIA
    private final CategoryService categoryService;

    // ---------------------------------------------------------------------
    // RETORNA TODAS AS CATEGORIAS CADASTRADAS
    // ---------------------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<CategoryDto>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    // ---------------------------------------------------------------------
    // RETORNA UMA CATEGORIA ESPECÍFICA PELO ID
    // ---------------------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    // ---------------------------------------------------------------------
    // CRIA UMA NOVA CATEGORIA
    // ---------------------------------------------------------------------
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryCreateDto categoryDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(categoryDto));
    }

    // ---------------------------------------------------------------------
    // ATUALIZA UMA CATEGORIA EXISTENTE PELO ID
    // ---------------------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @RequestBody CategoryCreateDto categoryDto) {
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryDto));
    }

    // ---------------------------------------------------------------------
    // EXCLUI UMA CATEGORIA PELO ID
    // ---------------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}