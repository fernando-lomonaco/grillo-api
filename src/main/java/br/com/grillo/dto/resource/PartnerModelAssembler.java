package br.com.grillo.model.resource;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import br.com.grillo.controller.PartnerController;
import br.com.grillo.model.PartnerModel;
import br.com.grillo.model.entity.Partner;

@Component
public class PartnerModelAssembler extends RepresentationModelAssemblerSupport<Partner, PartnerModel> {

    public PartnerModelAssembler() {
        super(PartnerController.class, PartnerModel.class);
    }

    @Override
    public PartnerModel toModel(Partner partner) {
        PartnerModel partnerModel = createModelWithId(partner.getCode(), partner);
        partnerModel.setCode(partner.getCode());
        partnerModel.setDocument(partner.getDocument());
        partnerModel.setName(partner.getName());
        partnerModel.setCreatedDate(partner.getCreatedDate());
        partnerModel.setUpdatedDate(partner.getUpdatedDate());
        return partnerModel;
    }

}
