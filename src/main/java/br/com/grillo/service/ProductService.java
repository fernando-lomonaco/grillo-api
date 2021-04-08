package br.com.grillo.service;

import br.com.grillo.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {

    Page<Product> all(Pageable pageable);

    Page<Product> findByCategory(Long categoryCode, Pageable pageable);

    Product save(Product product);

    Optional<Product> get(Long code);

    Product update(Product product, Long code);

    void delete(Long code);

}
