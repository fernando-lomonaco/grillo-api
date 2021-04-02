package br.com.grillo.controller;

import br.com.grillo.exception.EntityNotFoundException;
import br.com.grillo.model.ProductModel;
import br.com.grillo.model.entity.Product;
import br.com.grillo.model.resource.ProductModelAssembler;
import br.com.grillo.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static br.com.grillo.util.Constants.PRODUCT_NOT_FOUND;

@RestController
@RequestMapping(value = "products", produces = MediaTypes.HAL_JSON_VALUE)
public class ProductController {

    private final ProductService service;
    private final ProductModelAssembler assembler;

    public ProductController(ProductService service, ProductModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @Operation(summary = "Get all products")
    @ApiResponse(responseCode = "200", description = "Case the search ha been succeeded")
    @ApiResponse(responseCode = "404", description = "Case the search has been failure")
    @GetMapping
    public ResponseEntity<PagedModel<ProductModel>> all(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "20", required = false) int size,
            @RequestParam(value = "category", defaultValue = "") Long categoryCode,
            @RequestParam(value = "status", defaultValue = "", required = false) String status,
            PagedResourcesAssembler<Product> pagedAssembler) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Product> products = service.all(categoryCode, status, pageable);

        PagedModel<ProductModel> pagedModel = pagedAssembler.toModel(products, assembler);
        return new ResponseEntity<>(pagedModel, HttpStatus.OK);
    }

    @Operation(summary = "Get a product by its code")
    @ApiResponse(responseCode = "200", description = "Case the product has been found")
    @ApiResponse(responseCode = "404", description = "Case the product has not been found")
    @GetMapping("{code}")
    public ResponseEntity<ProductModel> get(@PathVariable final Long code) {
        return service.get(code)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND + code));
    }

    @Operation(summary = "Create a partner")
    @ApiResponse(responseCode = "201", description = "Case the product has been created")
    @PostMapping
    public ResponseEntity<ProductModel> post(@Valid @RequestBody ProductModel productModel) {

        final Product product = productModel.convertDTOToEntity();
        ProductModel model = assembler.toModel(service.save(product));
        return new ResponseEntity<>(model, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a product by its code")
    @ApiResponse(responseCode = "200", description = "Case the product has been found and updated")
    @ApiResponse(responseCode = "404", description = "Case the product has not been found")
    @PutMapping("{code}")
    public ResponseEntity<ProductModel> update(@PathVariable final Long code,
                                               @Valid @RequestBody ProductModel productModel) {
        return service.get(code).map(map -> {
            Product product = productModel.convertDTOToEntity();
            ProductModel model = assembler.toModel(service.update(product, code));
            return new ResponseEntity<>(model, HttpStatus.OK);
        }).orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND + code));
    }

    @Operation(summary = "Remove a product by its code")
    @ApiResponse(responseCode = "200", description = "Case the product has been found")
    @ApiResponse(responseCode = "204", description = "Case the product has been removed")
    @DeleteMapping("{code}")
    public ResponseEntity<Object> delete(@PathVariable final Long code) {
        return service.get(code).map(b -> {
            service.delete(code);
            return ResponseEntity.noContent().build();
        }).orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND + code));
    }

}
