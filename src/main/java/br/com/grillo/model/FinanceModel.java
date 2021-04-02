package br.com.grillo.model;

import br.com.grillo.model.entity.Finance;
import br.com.grillo.model.enums.FinanceType;
import br.com.grillo.util.validator.EnumNamePattern;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Relation(collectionRelation = "finances")
public class FinanceModel extends RepresentationModel<FinanceModel> {

    private Long code;
    @NotNull(message = "Data não pode ser vazia ou nula")
    private LocalDate buyDate;
    @NotNull(message = "Valor não pode ser vazio ou nulo")
    @Positive(message = "Valor deve ser maior que 0")
    private BigDecimal value;
    @NotNull(message = "Tipo não pode ser vazio ou nulo")
    @EnumNamePattern(regexp = "REVENUE|EXPENSE")
    private FinanceType financeType;
    @NotNull(message = "Parceiro não pode ser vazio ou nulo")
    private PartnerModel partner;
    @NotNull(message = "Produto não pode ser vazio ou nulo")
    private ProductModel product;

    public Finance convertDTOToEntity() {
        return new ModelMapper().map(this, Finance.class);
    }
}
