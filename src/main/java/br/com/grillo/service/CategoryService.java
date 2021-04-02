package br.com.grillo.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.grillo.model.entity.Category;

public interface CategoryService {

    Page<Category> all(String status, Pageable pageable);

    Category save(Category category);

    Optional<Category> get(Long code);

    Category update(Category category, Long code);

    void delete(Long code);

}