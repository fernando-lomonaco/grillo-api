package br.com.grillo.controller;

import br.com.grillo.exception.EntityNotFoundException;
import br.com.grillo.model.FinanceModel;
import br.com.grillo.model.entity.Finance;
import br.com.grillo.model.resource.FinanceModelAssembler;
import br.com.grillo.service.FinanceService;
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

import static br.com.grillo.util.Constants.FINANCE_NOT_FOUND;

@RestController
@Tag(name = "Finance", description = "Finance controller")
@RequestMapping("finances")
public class FinanceController {

    private final FinanceService service;
    private final FinanceModelAssembler assembler;

    public FinanceController(FinanceService service, FinanceModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @Operation(summary = "Get all finances")
    @ApiResponse(responseCode = "200", description = "Case the search ha been succeeded")
    @ApiResponse(responseCode = "404", description = "Case the search has been failure")
    @GetMapping
    public ResponseEntity<PagedModel<FinanceModel>> all(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "20", required = false) int size,
            @RequestParam(value = "product", defaultValue = "", required = false) Long productCode,
            @RequestParam(value = "partner", defaultValue = "", required = false) String partner,
            PagedResourcesAssembler<Finance> pagedAssembler) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Finance> finances = service.all(productCode, partner, pageable);
        PagedModel<FinanceModel> pagedModel = pagedAssembler.toModel(finances, assembler);
        return new ResponseEntity<>(pagedModel, HttpStatus.OK);
    }

    @Operation(summary = "Get a finance by its code")
    @ApiResponse(responseCode = "200", description = "Case the finance has been found")
    @ApiResponse(responseCode = "404", description = "Case the finance has not been found")
    @GetMapping("{code}")
    public ResponseEntity<FinanceModel> get(@PathVariable final Long code) {
        return service.get(code).map(assembler::toModel).map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException(FINANCE_NOT_FOUND + code));
    }

    @Operation(summary = "Create a finance")
    @ApiResponse(responseCode = "201", description = "Case the finance has been created")
    @PostMapping
    public ResponseEntity<FinanceModel> post(@Valid @RequestBody FinanceModel financeModel) {

        final Finance finance = financeModel.convertDTOToEntity();
        FinanceModel model = assembler.toModel(service.save(finance));
        return new ResponseEntity<>(model, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a finance by its code")
    @ApiResponse(responseCode = "200", description = "Case the finance has been found and updated")
    @ApiResponse(responseCode = "404", description = "Case the finance has not been found")
    @PutMapping("{code}")
    public ResponseEntity<FinanceModel> put(@PathVariable final Long code,
                                            @Valid @RequestBody FinanceModel financeModel) {
        return service.get(code).map(map -> {
            Finance finance = financeModel.convertDTOToEntity();
            FinanceModel model = assembler.toModel(service.update(finance, code));
            return new ResponseEntity<>(model, HttpStatus.OK);
        }).orElseThrow(() -> new EntityNotFoundException(FINANCE_NOT_FOUND + code));
    }

    @Operation(summary = "Remove a finance by its code")
    @ApiResponse(responseCode = "200", description = "Case the finance has been found")
    @ApiResponse(responseCode = "204", description = "Case the finance has been removed")
    @DeleteMapping("{code}")
    public ResponseEntity<Object> delete(@PathVariable final Long code) {
        return service.get(code).map(f -> {
            service.delete(code);
            return ResponseEntity.noContent().build();
        }).orElseThrow(() -> new EntityNotFoundException(FINANCE_NOT_FOUND + code));
    }

}
