package br.com.grillo.dto;

import br.com.grillo.model.Finance;
import br.com.grillo.enums.FinanceType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Relation(collectionRelation = "finances")
public class FinanceDTO extends RepresentationModel<FinanceDTO> {

    private Long code;

    @NotNull(message = "Data de compra não pode ser vazio ou nula.")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", locale = "en-US", timezone = "Brazil/East")
    private LocalDate buyDate;

    @NotNull(message = "Valor não pode ser vazio ou nulo.")
    @Positive(message = "Valor deve ser maior que 0.")
    private BigDecimal value;

    @NotNull(message = "Tipo não pode ser vazio ou nulo.")
    @Pattern(regexp="^(REVENUE|EXPENSE)$",
            message="Somente os tipos REVENUE ou EXPENSE são aceitos.")
    private FinanceType financeType;

    @NotNull(message = "Código do parceiro não pode ser vazio ou nulo.")
    private Long partnerCode;

    @NotNull(message = "Código do produto não pode ser vazio ou nulo.")
    private Long productCode;

    private UUID externalCode;

    public Finance convertDTOToEntity() {
        return new ModelMapper().map(this, Finance.class);
    }
}
