package br.com.grillo.model;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.grillo.model.entity.Partner;
import br.com.grillo.model.entity.Product;
import br.com.grillo.util.validator.ValidCNPJ;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hibernate.validator.constraints.br.CNPJ;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "partners")
public class PartnerModel extends RepresentationModel<PartnerModel> {

    private UUID code;
    @NotNull(message = "Nome não pode ser vazio ou nulo")
    @Size(min = 3, max = 60, message = "Nome deve ter entre 3 a 60 caracteres")
    private String name;
    @NotNull(message = "Documento não pode ser vazio ou nulo")
    @ValidCNPJ(message = "Já existe um cadastro com esse CNPJ")
    @CNPJ(message = "Documento com formato inválido")
    private String document;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public Partner convertDTOToEntity() {
        return new ModelMapper().map(this, Partner.class);
    }
}
