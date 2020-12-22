package br.com.grillo.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.grillo.model.entity.Finance;

public interface FinanceService {

    Page<Finance> all(String product, String partner, Pageable pageable);

    Finance save(Finance finance);

    Optional<Finance> get(String code);

    Finance update(Finance finance, String code);

    void delete(String code);

}