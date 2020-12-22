package br.com.grillo.service;

import br.com.grillo.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {

    Page<Product> all(String category, String status, Pageable pageable);

    Product save(Product product);

    Optional<Product> get(String code);

    Product update(Product product, String code);

    void delete(String code);
}
