package br.com.grillo.controller;

import br.com.grillo.dto.FinanceDTO;
import br.com.grillo.dto.resource.FinanceModelAssembler;
import br.com.grillo.exception.EntityNotFoundException;
import br.com.grillo.model.Finance;
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
@Tag(name = "API Finance", description = "Routes of finances")
@RequestMapping("finances")
public class FinanceController {

    private final FinanceService service;
    private final FinanceModelAssembler assembler;
    private final PagedResourcesAssembler<Finance> pagedAssembler;

    public FinanceController(FinanceService service, FinanceModelAssembler assembler, PagedResourcesAssembler<Finance> pagedAssembler) {
        this.service = service;
        this.assembler = assembler;
        this.pagedAssembler = pagedAssembler;
    }

    @Operation(summary = "Get all finances")
    @ApiResponse(responseCode = "200", description = "Case the search has been succeeded")
    @ApiResponse(responseCode = "404", description = "Case the search has been failed")
    @GetMapping
    public ResponseEntity<PagedModel<FinanceDTO>> all(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "20", required = false) int size
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Finance> finances = service.all(pageable);
        PagedModel<FinanceDTO> pagedModel = pagedAssembler.toModel(finances, assembler);
        return new ResponseEntity<>(pagedModel, HttpStatus.OK);
    }

    @Operation(summary = "Get a finance by its code")
    @ApiResponse(responseCode = "200", description = "Case the finance has been found")
    @ApiResponse(responseCode = "404", description = "Case the finance has not been found")
    @GetMapping("{code}")
    public ResponseEntity<FinanceDTO> get(@PathVariable final Long code) {
        return service.get(code).map(assembler::toModel).map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException(FINANCE_NOT_FOUND + code));
    }

    @Operation(summary = "Create a finance")
    @ApiResponse(responseCode = "201", description = "Case the finance has been created")
    @PostMapping
    public ResponseEntity<FinanceDTO> post(@Valid @RequestBody FinanceDTO financeDTO) {

        final Finance finance = financeDTO.convertDTOToEntity();
        FinanceDTO model = assembler.toModel(service.save(finance));
        return new ResponseEntity<>(model, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a finance by its code")
    @ApiResponse(responseCode = "200", description = "Case the finance has been found and updated")
    @ApiResponse(responseCode = "404", description = "Case the finance has not been found")
    @PutMapping("{code}")
    public ResponseEntity<FinanceDTO> put(@PathVariable final Long code,
                                          @Valid @RequestBody FinanceDTO financeDTO) {
        return service.get(code).map(map -> {
            Finance finance = financeDTO.convertDTOToEntity();
            FinanceDTO model = assembler.toModel(service.update(finance, code));
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
