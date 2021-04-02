package br.com.grillo.model.resource;

import br.com.grillo.controller.FinanceController;
import br.com.grillo.controller.PartnerController;
import br.com.grillo.controller.ProductController;
import br.com.grillo.model.FinanceModel;
import br.com.grillo.model.PartnerModel;
import br.com.grillo.model.ProductModel;
import br.com.grillo.model.entity.Finance;
import br.com.grillo.model.entity.Partner;
import br.com.grillo.model.entity.Product;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class FinanceModelAssembler extends RepresentationModelAssemblerSupport<Finance, FinanceModel> {

    public FinanceModelAssembler() {
        super(FinanceController.class, FinanceModel.class);
    }

    @Override
    public FinanceModel toModel(Finance finance) {
        FinanceModel financeModel = createModelWithId(finance.getCode(), finance);
        financeModel.setCode(finance.getCode());
        financeModel.setFinanceType(finance.getFinanceType());
        financeModel.setBuyDate(finance.getBuyDate());
        financeModel.setValue(finance.getValue());
        financeModel.setPartner(toPartnerModel(finance.getPartner()));
        financeModel.setProduct(toProductModel(finance.getProduct()));

        financeModel.add(linkTo(methodOn(ProductController.class).get(finance.getProduct().getCode()))
                .withRel("product"));
        financeModel.add(linkTo(methodOn(PartnerController.class).get(finance.getPartner().getCode()))
                .withRel("partner"));
        financeModel.add(linkTo(FinanceController.class).withRel("finances"));

        return financeModel;
    }

    private PartnerModel toPartnerModel(Partner partner) {
        return PartnerModel.builder()
                .code(partner.getCode())
                .name(partner.getName())
                .build();
    }

    private ProductModel toProductModel(Product product) {
        return ProductModel.builder()
                .code(product.getCode())
                .name(product.getName())
                .build();
    }

}
