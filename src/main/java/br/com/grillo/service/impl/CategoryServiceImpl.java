package br.com.grillo.service.impl;

import br.com.grillo.model.entity.Category;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;
    
    @Override
    @Cacheable(value = "categories", key = "#pageable.getPageNumber().toString()", unless = "#result==null or #result.isEmpty()")
    public Page<Category> all(String status, Pageable pageable) {
        return repository.findAll(pageable);
    }

    // @Cacheable(value = "findByStatus", key = "#status.concat('-').#pageable.getPageNumber().toString()", unless = "#result==null or #result.isEmpty()")
    public Page<Category> findByStatus(String status, Pageable pageable) {
        return repository.findByStatus(status.charAt(0), pageable);
    }

    @Override
    @Cacheable(value = "categoryId", key = "#code", unless = "#result==null")
    public Optional<Category> get(final String code) {
        return repository.findById(UUID.fromString(code));
    }

    @Override
    @CacheEvict(value = {"categories"}, allEntries = true)
    public Category save(final Category categoryRequest) {
        return repository.save(categoryRequest);
    }

    @Override
    @Caching(evict = @CacheEvict(value = {"categories"}, allEntries = true), put = @CachePut(value = "categoryId", key = "#code"))
    public Category update(final Category categoryRequest, final String code) {
        categoryRequest.setCode(UUID.fromString(code));
        return repository.save(categoryRequest);
    }

    @Override
    @CacheEvict(value = {"categories", "categoryId"}, allEntries = true)
    public void delete(final String code) {
        repository.deleteById(UUID.fromString(code));
    }
}
