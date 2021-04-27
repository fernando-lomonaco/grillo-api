package br.com.grillo.service.impl;

import java.util.Optional;

import br.com.grillo.service.PartnerService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.grillo.model.Partner;
import br.com.grillo.repository.PartnerRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PartnerServiceImpl implements PartnerService {

    private final PartnerRepository repository;

    @Override
    @Cacheable(value = "partners", unless = "#result==null or #result.isEmpty()", key = "#pageable.getPageNumber().toString()")
    public Page<Partner> all(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Partner> findByDocument(final String document) {
        return repository.findByDocument(document);
    }

    @Override
    @Cacheable(value = "partnerId", key = "#code", unless = "#result==null")
    public Optional<Partner> get(final Long code) {
        return repository.findById(code);
    }

    @Override
    @CacheEvict(value = "partners", allEntries = true)
    public Partner save(final Partner partnerRequest) {
        return repository.save(partnerRequest);
    }

    @Override
    @Caching(evict = @CacheEvict(value = "partners", allEntries = true), put = @CachePut(value = "partnerId", key = "#code"))
    public Partner update(final Partner partnerRequest, final Long code) {
        partnerRequest.setCode(code);
        return repository.save(partnerRequest);
    }

    @Override
    @CacheEvict(value = { "partners", "partnerId" }, allEntries = true)
    public void delete(final Long code) {
        repository.deleteById(code);
    }

}
