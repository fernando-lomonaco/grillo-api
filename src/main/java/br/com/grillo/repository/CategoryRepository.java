package br.com.grillo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import br.com.grillo.model.Category;

@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {

    Page<Category> findByStatus(char status, Pageable pageable);

}
