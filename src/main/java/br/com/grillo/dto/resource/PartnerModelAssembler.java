package br.com.grillo.dto.resource;

import br.com.grillo.controller.PartnerController;
import br.com.grillo.dto.PartnerDTO;
import br.com.grillo.model.Partner;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class PartnerModelAssembler extends RepresentationModelAssemblerSupport<Partner, PartnerDTO> {

    public PartnerModelAssembler() {
        super(PartnerController.class, PartnerDTO.class);
    }

    @Override
    public PartnerDTO toModel(Partner partner) {
        PartnerDTO partnerDTO = buildPartnerModel(partner);
        partnerDTO.add((WebMvcLinkBuilder.linkTo(PartnerController.class).slash(partner.getCode())).withSelfRel());
        partnerDTO.add((WebMvcLinkBuilder.linkTo(PartnerController.class).slash("document/"+partner.getDocument())).withSelfRel());
        return partnerDTO;
    }

    private PartnerDTO buildPartnerModel(Partner partner) {
        return PartnerDTO.builder()
                .code(partner.getCode())
                .name(partner.getName())
                .document(partner.getDocument())
                .createdDate(partner.getCreatedDate())
                .updatedDate(partner.getUpdatedDate())
                .externalCode(partner.getExternalCode())
                .build();
    }

}
