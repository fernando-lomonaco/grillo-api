package br.com.grillo.controller;

import br.com.grillo.dto.response.Response;
import br.com.grillo.exception.EntityNotFoundException;
import br.com.grillo.dto.CategoryDTO;
import br.com.grillo.model.Category;
import br.com.grillo.dto.resource.CategoryModelAssembler;
import br.com.grillo.service.CategoryService;
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

import static br.com.grillo.util.Constants.CATEGORY_NOT_FOUND;

@RestController
@Tag(name = "API Category", description = "Routes of categories")
@RequestMapping("categories")
public class CategoryController {

    private final CategoryService service;
    private final CategoryModelAssembler assembler;

    public CategoryController(CategoryService service, CategoryModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @Operation(summary = "Get all categories")
    @ApiResponse(responseCode = "200", description = "Case the search has been succeeded")
    @ApiResponse(responseCode = "404", description = "Case the search has been failed")
    @GetMapping
    public ResponseEntity<Response<PagedModel<CategoryDTO>>> all(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "20", required = false) int size,
            @RequestParam(value = "status", defaultValue = "", required = false) String status,
            PagedResourcesAssembler<Category> pagedAssembler) {

        Response<PagedModel<CategoryDTO>> response = new Response<>();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Category> categories = service.all(status, pageable);

        PagedModel<CategoryDTO> pagedModel = pagedAssembler.toModel(categories, assembler);
        response.setData(pagedModel);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get a category by its code")
    @ApiResponse(responseCode = "200", description = "Case the category has been found")
    @ApiResponse(responseCode = "404", description = "Case the category has not been found")
    @GetMapping("{code}")
    public ResponseEntity<Response<CategoryDTO>> get(@PathVariable final Long code) {
        Response<CategoryDTO> response = new Response<>();

        return service.get(code)
                .map(map -> {
                    CategoryDTO model = assembler.toModel(map);
                    response.setData(model);
                    return ResponseEntity.ok().body(response);
                }).orElseThrow(() -> new EntityNotFoundException(CATEGORY_NOT_FOUND + code));
    }

    @Operation(summary = "Create a category")
    @ApiResponse(responseCode = "201", description = "Case the category has been created")
    @PostMapping
    public ResponseEntity<Response<CategoryDTO>> post(@Valid @RequestBody CategoryDTO categoryDTO) {

        Response<CategoryDTO> response = new Response<>();
        final Category category = categoryDTO.convertDTOToEntity();
        category.setExternalCode(UUID.randomUUID());
        CategoryDTO model = assembler.toModel(service.save(category));
        response.setData(model);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a category by its code")
    @ApiResponse(responseCode = "200", description = "Case the category has been found and updated")
    @ApiResponse(responseCode = "404", description = "Case the category has not been found")
    @PutMapping("{code}")
    public ResponseEntity<Response<CategoryDTO>> update(@PathVariable final Long code,
                                                        @Valid @RequestBody CategoryDTO categoryDTO) {

        Response<CategoryDTO> response = new Response<>();
        return service.get(code)
                .map(c -> {
                    Category category = categoryDTO.convertDTOToEntity();
                    category.setExternalCode(c.getExternalCode());
                    CategoryDTO model = assembler.toModel(service.update(category, code));
                    response.setData(model);
                    return ResponseEntity.ok().body(response);
                }).orElseThrow(() -> new EntityNotFoundException(CATEGORY_NOT_FOUND + code));
    }

}
