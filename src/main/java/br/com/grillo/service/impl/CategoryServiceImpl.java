package br.com.grillo.service.impl;

import br.com.grillo.model.Category;
import br.com.grillo.repository.CategoryRepository;
import br.com.grillo.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

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
    @Caching(put = @CachePut(value = "categoryId", key = "#code"),
            evict = @CacheEvict(value = "categories", allEntries = true))
    public Category update(final Category categoryRequest, final Long code) {
        categoryRequest.setCode(code);
        return repository.save(categoryRequest);
    }

    @Override
    @CacheEvict(value = {"categories", "categoryId"}, allEntries = true)
    public void delete(final Long code) {
        repository.deleteById(code);
    }
}
