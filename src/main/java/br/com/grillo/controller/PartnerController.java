package br.com.grillo.controller;

import br.com.grillo.dto.PartnerDTO;
import br.com.grillo.dto.resource.PartnerModelAssembler;
import br.com.grillo.dto.response.Response;
import br.com.grillo.exception.EntityNotFoundException;
import br.com.grillo.model.Partner;
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

import java.util.UUID;

import static br.com.grillo.util.Constants.PARTNER_NOT_FOUND;

@RestController
@Tag(name = "API Partner", description = "Routes of partners")
@RequestMapping("partners")
public class PartnerController {

    private final PartnerService service;
    private final PartnerModelAssembler assembler;
    private final PagedResourcesAssembler<Partner> pagedAssembler;

    public PartnerController(PartnerService service, PartnerModelAssembler assembler, PagedResourcesAssembler<Partner> pagedAssembler) {
        this.service = service;
        this.assembler = assembler;
        this.pagedAssembler = pagedAssembler;
    }

    @Operation(summary = "Get all partners")
    @ApiResponse(responseCode = "200", description = "Case the search has been succeeded")
    @ApiResponse(responseCode = "404", description = "Case the search has been failed")
    @GetMapping
    public ResponseEntity<Response<PagedModel<PartnerDTO>>> all(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "20", required = false) int size) {

        Response<PagedModel<PartnerDTO>> response = new Response<>();

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Partner> partners = service.all(pageable);

        PagedModel<PartnerDTO> pagedModel = pagedAssembler.toModel(partners, assembler);
        response.setData(pagedModel);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get a partner by its code")
    @ApiResponse(responseCode = "200", description = "Case the partner has been found")
    @ApiResponse(responseCode = "404", description = "Case the partner has not been found")
    @GetMapping("document/{document}")
    public ResponseEntity<Response<PartnerDTO>> getByDocument(@PathVariable final String document) {
        Response<PartnerDTO> response = new Response<>();

        return service.findByDocument(document)
                .map(map -> {
                    PartnerDTO model = assembler.toModel(map);
                    response.setData(model);
                    return ResponseEntity.ok().body(response);
                }).orElseThrow(() -> new EntityNotFoundException(PARTNER_NOT_FOUND + document));
    }

    @Operation(summary = "Get a partner by its code")
    @ApiResponse(responseCode = "200", description = "Case the partner has been found")
    @ApiResponse(responseCode = "404", description = "Case the partner has not been found")
    @GetMapping("{code}")
    public ResponseEntity<Response<PartnerDTO>> get(@PathVariable final Long code) {
        Response<PartnerDTO> response = new Response<>();

        return service.get(code)
                .map(map -> {
                    PartnerDTO model = assembler.toModel(map);
                    response.setData(model);
                    return ResponseEntity.ok().body(response);
                }).orElseThrow(() -> new EntityNotFoundException(PARTNER_NOT_FOUND + code));
    }

    @Operation(summary = "Create a partner")
    @ApiResponse(responseCode = "201", description = "Case the partner has been created")
    @PostMapping
    public ResponseEntity<Response<PartnerDTO>> post(@Valid @RequestBody PartnerDTO partnerDTO) {
        Response<PartnerDTO> response = new Response<>();

        final Partner partner = partnerDTO.convertDTOToEntity();
        partner.setExternalCode(UUID.randomUUID());
        PartnerDTO model = assembler.toModel(service.save(partner));
        response.setData(model);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a partner by its code")
    @ApiResponse(responseCode = "200", description = "Case the partner has been found and updated")
    @ApiResponse(responseCode = "404", description = "Case the partner has not been found")
    @PutMapping("{code}")
    public ResponseEntity<Response<PartnerDTO>> update(@PathVariable final Long code,
                                                       @Valid @RequestBody PartnerDTO partnerDTO) {
        Response<PartnerDTO> response = new Response<>();

        return service.get(code).map(p -> {
            Partner partner = partnerDTO.convertDTOToEntity();
            partner.setExternalCode(p.getExternalCode());
            PartnerDTO model = assembler.toModel(service.update(partner, code));
            response.setData(model);
            return ResponseEntity.ok().body(response);
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
