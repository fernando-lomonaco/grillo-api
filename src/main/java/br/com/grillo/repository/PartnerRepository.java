package br.com.grillo.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import br.com.grillo.model.Partner;

@Repository
public interface PartnerRepository extends PagingAndSortingRepository<Partner, Long> {

    Optional<Partner> findByDocument(String document);
}
