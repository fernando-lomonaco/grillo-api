package br.com.grillo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import br.com.grillo.model.Finance;

@Repository
public interface FinanceRepository extends PagingAndSortingRepository<Finance, Long> {

    Page<Finance> findByProductCode(Long productCode, Pageable pageable);

}
