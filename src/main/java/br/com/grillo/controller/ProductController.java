package br.com.grillo.controller;

import br.com.grillo.dto.ProductDTO;
import br.com.grillo.dto.resource.ProductModelAssembler;
import br.com.grillo.dto.response.Response;
import br.com.grillo.exception.EntityNotFoundException;
import br.com.grillo.model.Product;
import br.com.grillo.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

import static br.com.grillo.util.Constants.PRODUCT_NOT_FOUND;

@RestController
@Tag(name = "API Product", description = "Routes of products")
@RequestMapping(value = "products")
public class ProductController {

    private final ProductService service;
    private final ProductModelAssembler assembler;
    private final PagedResourcesAssembler<Product> pagedAssembler;

    public ProductController(ProductService service, ProductModelAssembler assembler, PagedResourcesAssembler<Product> pagedAssembler) {
        this.service = service;
        this.assembler = assembler;
        this.pagedAssembler = pagedAssembler;
    }

    @Operation(summary = "Get all products")
    @ApiResponse(responseCode = "200", description = "Case the search has been succeeded")
    @ApiResponse(responseCode = "404", description = "Case the search has been failed")
    @GetMapping()
    public ResponseEntity<Response<PagedModel<ProductDTO>>> all(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "20", required = false) int size) {

        Response<PagedModel<ProductDTO>> response = new Response<>();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Product> products = service.all(pageable);

        PagedModel<ProductDTO> pagedModel = pagedAssembler.toModel(products, assembler);
        response.setData(pagedModel);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get all products by category")
    @ApiResponse(responseCode = "200", description = "Case the search has been succeeded")
    @ApiResponse(responseCode = "404", description = "Case the search has been failed")
    @GetMapping("/byCategory/{category}")
    public ResponseEntity<Response<PagedModel<ProductDTO>>> findAllByCategory(
            @PathVariable("category") Long categoryCode,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "20", required = false) int size) {

        Response<PagedModel<ProductDTO>> response = new Response<>();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Product> products = service.findAllByCategory(categoryCode, pageable);
        if (products.isEmpty()) {
            throw new EntityNotFoundException("There are no products registered with the category=" + categoryCode);
        }

        PagedModel<ProductDTO> pagedModel = pagedAssembler.toModel(products, assembler);
        response.setData(pagedModel);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get a product by its code")
    @ApiResponse(responseCode = "200", description = "Case the product has been found")
    @ApiResponse(responseCode = "404", description = "Case the product has not been found")
    @GetMapping("{code}")
    public ResponseEntity<Response<ProductDTO>> get(@PathVariable final Long code) {
        Response<ProductDTO> response = new Response<>();

        return service.get(code)
                .map(map -> {
                    ProductDTO model = assembler.toModel(map);
                    response.setData(model);
                    return ResponseEntity.ok().body(response);
                }).orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND + code));
    }

    @Operation(summary = "Create a product")
    @ApiResponse(responseCode = "201", description = "Case the product has been created")
    @PostMapping
    public ResponseEntity<Response<ProductDTO>> post(@Valid @RequestBody ProductDTO productDTO) {
        Response<ProductDTO> response = new Response<>();

        final Product product = productDTO.convertDTOToEntity();
        product.setExternalCode(UUID.randomUUID());
        ProductDTO model = assembler.toModel(service.save(product));
        response.setData(model);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a product by its code")
    @ApiResponse(responseCode = "200", description = "Case the product has been found and updated")
    @ApiResponse(responseCode = "404", description = "Case the product has not been found")
    @PutMapping("{code}")
    public ResponseEntity<Response<ProductDTO>> update(@PathVariable final Long code,
                                                       @Valid @RequestBody ProductDTO productDTO) {
        Response<ProductDTO> response = new Response<>();

        return service.get(code).map(p -> {
            Product product = productDTO.convertDTOToEntity();
            product.setExternalCode(p.getExternalCode());
            ProductDTO model = assembler.toModel(service.update(product, code));
            response.setData(model);
            return ResponseEntity.ok().body(response);
        }).orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND + code));
    }

    @Operation(summary = "Remove a product by its code")
    @ApiResponse(responseCode = "200", description = "Case the product has been found")
    @ApiResponse(responseCode = "204", description = "Case the product has been removed")
    @DeleteMapping("{code}")
    public ResponseEntity<Response<String>> delete(@PathVariable final Long code) {
        Response<String> response = new Response<>();
        return service.get(code).map(p -> {
            service.delete(code);
            response.setData("Product id=" + p.getCode() + " successfully deleted");
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        }).orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND + code));
    }

}
