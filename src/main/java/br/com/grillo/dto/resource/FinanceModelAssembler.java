package br.com.grillo.dto.resource;

import br.com.grillo.controller.FinanceController;
import br.com.grillo.controller.PartnerController;
import br.com.grillo.controller.ProductController;
import br.com.grillo.dto.FinanceDTO;
import br.com.grillo.model.Finance;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class FinanceModelAssembler extends RepresentationModelAssemblerSupport<Finance, FinanceDTO> {

    public FinanceModelAssembler() {
        super(FinanceController.class, FinanceDTO.class);
    }

    @Override
    public FinanceDTO toModel(Finance finance) {
        FinanceDTO financeDTO = buildFinanceModel(finance);
        financeDTO.add(linkTo(methodOn(FinanceController.class).all(0, 20)).withRel("finances"));
        financeDTO.add(linkTo(methodOn(ProductController.class).get(finance.getProduct().getCode()))
                .withRel("product"));
        financeDTO.add(linkTo(methodOn(PartnerController.class).get(finance.getPartner().getCode()))
                .withRel("partner"));
        financeDTO.add((WebMvcLinkBuilder.linkTo(FinanceController.class).slash(finance.getCode())).withSelfRel());

        return financeDTO;
    }

    private FinanceDTO buildFinanceModel(Finance finance) {

        return FinanceDTO.builder()
                .code(finance.getCode())
                .financeType(finance.getFinanceType())
                .buyDate(finance.getBuyDate())
                .value(finance.getValue())
                .partnerCode(finance.getPartner().getCode())
                .productCode(finance.getProduct().getCode())
                .build();
    }


}
