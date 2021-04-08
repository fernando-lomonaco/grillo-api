package br.com.grillo.service;

import br.com.grillo.model.Partner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PartnerService {

    Page<Partner> all(Pageable pageable);

    Partner save(Partner partner);

    Optional<Partner> get(Long code);

    Partner update(Partner partner, Long code);

    Optional<String> findByDocument(String cnpj);

    void delete(Long code);
}
