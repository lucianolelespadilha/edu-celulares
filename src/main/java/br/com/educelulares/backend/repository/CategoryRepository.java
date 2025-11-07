package br.com.educelulares.backend.repository;

import br.com.educelulares.backend.entity.Category;
import br.com.educelulares.backend.entity.Product;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
