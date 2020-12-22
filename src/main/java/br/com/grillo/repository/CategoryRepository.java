package br.com.grillo.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.grillo.model.entity.Category;

@Repository
@Transactional(readOnly = true)
public interface CategoryRepository extends PagingAndSortingRepository<Category, UUID> {

    Page<Category> findByStatus(char status, Pageable pageable);

}
