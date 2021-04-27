package br.com.grillo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import br.com.grillo.model.Product;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

      Page<Product> findByCategoryCode(Long categoryCode, Pageable pageable);
}
