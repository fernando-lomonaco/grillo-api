package br.com.grillo.service.impl;

import java.util.Optional;
import java.util.UUID;

import br.com.grillo.service.PartnerService;
import br.com.grillo.service.ProductService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.grillo.model.entity.Product;
import br.com.grillo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    @Override
    @Cacheable(value = "products", unless = "#result==null or #result.isEmpty()", key = "#category.concat('-').concat(#pageable.getPageNumber().toString())")
    public Page<Product> all(Long categoryCode, String status, Pageable pageable) {
        return repository.findByCategoryCodeAndStatus(categoryCode, status.toUpperCase().charAt(0), pageable);
    }

    //@Cacheable(value = "findByStatus", unless = "#result==null or #result.isEmpty()", key = "#status.concat('-').#pageable.getPageNumber().toString()")
    // public Page<Product> findByStatus(String status, Pageable pageable) {
    //     return repository.findByStatus(status.toUpperCase().charAt(0), pageable);
    //  }

    @Override
    @Cacheable(value = "productId", key = "#code", unless = "#result==null")
    public Optional<Product> get(final Long code) {
        return repository.findById(code);
    }

    @Override
    @CacheEvict(value = {"products"}, allEntries = true)
    public Product save(final Product productRequest) {
        return repository.save(productRequest);
    }

    @Override
    @Caching(evict = @CacheEvict(value = {"products"}, allEntries = true), put = @CachePut(value = "productId", key = "#code"))
    public Product update(final Product productRequest, final Long code) {
        productRequest.setCode(code);
        return repository.save(productRequest);
    }

    @Override
    @CacheEvict(value = {"products", "productId"}, allEntries = true)
    public void delete(final Long code) {
        repository.deleteById(code);
    }

}
