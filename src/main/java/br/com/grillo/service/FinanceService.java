package br.com.grillo.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.grillo.model.Finance;

public interface FinanceService {

    Page<Finance> all(Pageable pageable);

    Finance save(Finance finance);

    Optional<Finance> get(Long code);

    Finance update(Finance finance, Long code);

    void delete(Long code);

}