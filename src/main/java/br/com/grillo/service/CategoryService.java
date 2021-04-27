package br.com.grillo.service;

import br.com.grillo.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CategoryService {

    Page<Category> all(String status, Pageable pageable);

    Category save(Category category);

    Optional<Category> get(Long code);

    Category update(Category category, Long code);

    void delete(Long code);

}