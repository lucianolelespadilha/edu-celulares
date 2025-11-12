package br.com.educelulares.backend.service;

import br.com.educelulares.backend.dto.CategoryCreateDto;
import br.com.educelulares.backend.dto.CategoryDto;
import br.com.educelulares.backend.entity.Category;
import br.com.educelulares.backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // CRIA UMA NOVA CATEGORIA
    public CategoryDto createCategory(CategoryCreateDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.name());
        category.setDescription(categoryDto.description());
        Category savedCategory = categoryRepository.save(category);
        return new CategoryDto(savedCategory.getId(), savedCategory.getName(), savedCategory.getDescription());
    }

    // LISTA TODAS AS CATEGORIAS
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(category -> new CategoryDto(category.getId(), category.getName(), category.getDescription()))
                .collect(Collectors.toList());
    }

    // BUSCA UMA CATEGORIA PELO ID
    public CategoryDto findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return new CategoryDto(category.getId(), category.getName(), category.getDescription());
    }

    // ATUALIZA UMA CATEGORIA EXISTENTE PELO ID
    public CategoryDto updateCategory(Long id, CategoryCreateDto categoryCreateDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(categoryCreateDto.name());
        category.setDescription(categoryCreateDto.description());
        Category updatedCategory = categoryRepository.save(category);
        return new CategoryDto(updatedCategory.getId(), updatedCategory.getName(), updatedCategory.getDescription());
    }

    // EXCLUI UMA CATEGORIA PELO ID
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found");
        }
        categoryRepository.deleteById(id);
    }
}