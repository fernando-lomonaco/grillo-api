package br.com.grillo.service.impl;

import br.com.grillo.model.Finance;
import br.com.grillo.repository.FinanceRepository;
import br.com.grillo.service.FinanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FinanceServiceImpl implements FinanceService {

    private final FinanceRepository repository;

    @Override
    @Cacheable(value = "finances", key = "#pageable.getPageNumber().toString()", unless = "#result==null or #result.isEmpty()")
    public Page<Finance> all(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    @Cacheable(value = "financeId", key = "#code")
    public Optional<Finance> get(final Long code) {
        return repository.findById(code);
    }

    @Override
    @CacheEvict(value = {"finances", "financesByProductOrPartner"}, allEntries = true)
    public Finance save(final Finance financeRequest) {
        financeRequest.setExternalCode(UUID.randomUUID());
        return repository.save(financeRequest);
    }

    @Override
    @Caching(evict = @CacheEvict(value = {"finances", "financesByProductOrPartner"}, allEntries = true), put = @CachePut(value = "financeId", key = "#code"))
    public Finance update(final Finance financeRequest, final Long code) {
        financeRequest.setCode(code);
        return repository.save(financeRequest);
    }

    @Override
    @CacheEvict(value = {"finances", "financesByProductOrPartner", "financeId"}, allEntries = true)
    public void delete(final Long code) {
        repository.deleteById(code);
    }

}
