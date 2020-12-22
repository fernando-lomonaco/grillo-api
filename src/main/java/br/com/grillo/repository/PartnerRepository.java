package br.com.grillo.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import br.com.grillo.model.entity.Partner;

@Repository
public interface PartnerRepository extends PagingAndSortingRepository<Partner, UUID> {
    Optional<String> findByDocument(String cnpj);
}
