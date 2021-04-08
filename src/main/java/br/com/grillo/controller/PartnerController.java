package br.com.grillo.controller;

import br.com.grillo.exception.EntityNotFoundException;
import br.com.grillo.dto.PartnerDTO;
import br.com.grillo.model.Partner;
import br.com.grillo.dto.resource.PartnerModelAssembler;
import br.com.grillo.service.PartnerService;
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

import static br.com.grillo.util.Constants.PARTNER_NOT_FOUND;

@RestController
@Tag(name = "API Partner", description = "Routes of partners")
@RequestMapping("partners")
public class PartnerController {

    private final PartnerService service;
    private final PartnerModelAssembler assembler;

    public PartnerController(PartnerService service, PartnerModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @Operation(summary = "Get all partners")
    @ApiResponse(responseCode = "200", description = "Case the search has been succeeded")
    @ApiResponse(responseCode = "404", description = "Case the search has been failed")
    @GetMapping
    public ResponseEntity<PagedModel<PartnerDTO>> all(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "20", required = false) int size,
            PagedResourcesAssembler<Partner> pagedAssembler) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Partner> partners = service.all(pageable);
        PagedModel<PartnerDTO> pagedModel = pagedAssembler.toModel(partners, assembler);
        return new ResponseEntity<>(pagedModel, HttpStatus.OK);
    }

    @Operation(summary = "Get a partner by its code")
    @ApiResponse(responseCode = "200", description = "Case the partner has been found")
    @ApiResponse(responseCode = "404", description = "Case the partner has not been found")
    @GetMapping("{code}")
    public ResponseEntity<PartnerDTO> get(@PathVariable final Long code) {
        return service.get(code)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException(PARTNER_NOT_FOUND + code));
    }

    @Operation(summary = "Create a partner")
    @ApiResponse(responseCode = "201", description = "Case the partner has been created")
    @PostMapping
    public ResponseEntity<PartnerDTO> post(@Valid @RequestBody PartnerDTO partnerDTO) {

        final Partner partner = partnerDTO.convertDTOToEntity();
        PartnerDTO model = assembler.toModel(service.save(partner));
        return new ResponseEntity<>(model, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a partner by its code")
    @ApiResponse(responseCode = "200", description = "Case the partner has been found and updated")
    @ApiResponse(responseCode = "404", description = "Case the partner has not been found")
    @PutMapping("{code}")
    public ResponseEntity<PartnerDTO> put(@PathVariable final Long code,
                                          @Valid @RequestBody PartnerDTO partnerDTO) {
        return service.get(code).map(map -> {
            Partner partner = partnerDTO.convertDTOToEntity();
            PartnerDTO model = assembler.toModel(service.update(partner, code));
            return new ResponseEntity<>(model, HttpStatus.OK);
        }).orElseThrow(() -> new EntityNotFoundException(PARTNER_NOT_FOUND + code));
    }

    @Operation(summary = "Remove a partner by its code")
    @ApiResponse(responseCode = "200", description = "Case the partner has been found")
    @ApiResponse(responseCode = "204", description = "Case the partner has been removed")
    @DeleteMapping("{code}")
    public ResponseEntity<Object> delete(@PathVariable final Long code) {
        return service.get(code).map(b -> {
            service.delete(code);
            return ResponseEntity.noContent().build();
        }).orElseThrow(() -> new EntityNotFoundException(PARTNER_NOT_FOUND + code));
    }
}
