package br.com.grillo.service.impl;

import br.com.grillo.client.GardeClient;
import br.com.grillo.exception.ConnectException;
import br.com.grillo.model.Category;
import br.com.grillo.repository.CategoryRepository;
import br.com.grillo.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;
    private final GardeClient gardeClient;

    private static final String KEY = "security.show.edit.category";

    @Override
    @Cacheable(value = "categories", key = "#pageable.getPageNumber().toString()", unless = "#result==null or #result.isEmpty()")
    public Page<Category> all(String status, Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    @Cacheable(value = "categoryId", key = "#code", unless = "#result==null")
    public Optional<Category> get(final Long code) {
        return repository.findById(code);
    }

    @Override
    @CacheEvict(value = "categories", allEntries = true)
    public Category save(final Category categoryRequest) {
        return repository.save(categoryRequest);
    }

    @Override
    @Caching(put = @CachePut(value = "categoryId", key = "#code"), evict = @CacheEvict(value = "categories", allEntries = true))
    public Category update(final Category categoryRequest, final Long code) {
        try {
            var value = gardeClient.connectClient(KEY);
            if ("S".equalsIgnoreCase(value)) {
                categoryRequest.setCode(code);
                return repository.save(categoryRequest);
            }

            log.info("The Security Service's {} key has value {}", KEY, value);
        } catch (InterruptedException | IOException e) {
            throw new ConnectException("Client Service unavailable - " + e);
        }
        return this.get(code).get();

    }

    @Override
    @CacheEvict(value = { "categories", "categoryId" }, allEntries = true)
    public void delete(final Long code) {
        repository.deleteById(code);
    }
}
