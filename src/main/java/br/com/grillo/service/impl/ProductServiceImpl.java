package br.com.grillo.service.impl;

import br.com.grillo.exception.EntityNotFoundException;
import br.com.grillo.model.Product;
import br.com.grillo.repository.CategoryRepository;
import br.com.grillo.repository.ProductRepository;
import br.com.grillo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static br.com.grillo.util.Constants.CATEGORY_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;

    @Override
    @Cacheable(value = "products", key = "#pageable.getPageNumber().toString()", unless = "#result==null or #result.isEmpty()")
    public Page<Product> all(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    @Cacheable(value = "products", key = "#categoryCode.toString().concat('-').concat(#pageable.getPageNumber().toString())", unless = "#result==null or #result.isEmpty()")
    public Page<Product> findByCategory(Long categoryCode, Pageable pageable) {
        return repository.findByCategoryCode(categoryCode, pageable);
    }

    @Override
    @Cacheable(value = "productId", key = "#code", unless = "#result==null")
    public Optional<Product> get(final Long code) {
        return repository.findById(code);
    }

    @Override
    @CacheEvict(value = "products", allEntries = true)
    public Product save(final Product productRequest) {
        return categoryRepository.findById(productRequest.getCategory().getCode())
                .map(x -> repository.save(productRequest))
                .orElseThrow(() -> new EntityNotFoundException(CATEGORY_NOT_FOUND + productRequest.getCategory().getCode()));
    }

    @Override
    @Caching(evict = @CacheEvict(value = "products", allEntries = true), put = @CachePut(value = "productId", key = "#code"))
    public Product update(final Product productRequest, final Long code) {
        return categoryRepository.findById(productRequest.getCategory().getCode())
                .map(x -> {
                    productRequest.setCode(code);
                    return repository.save(productRequest);
                })
                .orElseThrow(() -> new EntityNotFoundException(CATEGORY_NOT_FOUND + productRequest.getCategory().getCode()));

    }

    @Override
    @CacheEvict(value = {"products", "productId"}, allEntries = true)
    public void delete(final Long code) {
        repository.deleteById(code);
    }

}
