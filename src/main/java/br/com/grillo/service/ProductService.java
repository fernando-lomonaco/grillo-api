package br.com.grillo.service;

import br.com.grillo.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {

    Page<Product> all(Long category, String status, Pageable pageable);

    Product save(Product product);

    Optional<Product> get(Long code);

    Product update(Product product, Long code);

    void delete(Long code);
}
