package br.com.grillo.controller;

import br.com.grillo.exception.EntityNotFoundException;
import br.com.grillo.model.CategoryModel;
import br.com.grillo.model.entity.Category;
import br.com.grillo.model.resource.CategoryModelAssembler;
import br.com.grillo.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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

import static br.com.grillo.util.Constants.CATEGORY_NOT_FOUND;

@RestController
@Tag(name = "Category", description = "Category controller")
@RequestMapping("categories")
public class CategoryController {

    private final CategoryService service;
    private final CategoryModelAssembler assembler;

    @Autowired
    public CategoryController(CategoryService service, CategoryModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @Operation(summary = "Get all categories")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Case the search ha been succeeded"),
            @ApiResponse(responseCode = "404", description = "Case the search has been failure") })
    @GetMapping
    public ResponseEntity<PagedModel<CategoryModel>> all(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "20", required = false) int size,
            @RequestParam(value = "status", defaultValue = "", required = false) String status,
            PagedResourcesAssembler<Category> pagedAssembler) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Category> categories = service.all(status, pageable);

        PagedModel<CategoryModel> pagedModel = pagedAssembler.toModel(categories, assembler);
        return new ResponseEntity<>(pagedModel, HttpStatus.OK);
    }

    @Operation(summary = "Get a category by its code")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Case the category has been found"),
            @ApiResponse(responseCode = "404", description = "Case the category has not been found") })
    @GetMapping("{code}")
    public ResponseEntity<CategoryModel> get(@PathVariable final String code) {
        return service.get(code).map(assembler::toModel).map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException(CATEGORY_NOT_FOUND + code));
    }

    @Operation(summary = "Create a category")
    @ApiResponse(responseCode = "201", description = "Case the category has been created")
    @PostMapping
    public ResponseEntity<CategoryModel> post(@Valid @RequestBody CategoryModel categoryModel) {

        final Category category = categoryModel.convertDTOToEntity();
        CategoryModel model = assembler.toModel(service.save(category));
        return new ResponseEntity<>(model, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a category by its code")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Case the category has been found and updated"),
            @ApiResponse(responseCode = "404", description = "Case the category has not been found") })
    @PutMapping("{code}")
    public ResponseEntity<CategoryModel> update(@PathVariable final String code,
            @Valid @RequestBody CategoryModel categoryModel) {
        return service.get(code).map(map -> {
            Category category = categoryModel.convertDTOToEntity();
            CategoryModel model = assembler.toModel(service.update(category, code));
            return new ResponseEntity<>(model, HttpStatus.OK);
        }).orElseThrow(() -> new EntityNotFoundException(CATEGORY_NOT_FOUND + code));
    }

}
