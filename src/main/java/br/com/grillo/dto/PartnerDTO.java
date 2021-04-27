package br.com.grillo.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.grillo.model.Partner;
import br.com.grillo.util.validator.ValidCNPJ;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.*;
import org.hibernate.validator.constraints.br.CNPJ;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Relation(collectionRelation = "partners")
public class PartnerDTO extends RepresentationModel<PartnerDTO> {

    private Long code;

    @NotNull(message = "Nome não pode ser vazio ou nulo.")
    @Size(min = 3, max = 60, message = "Nome deve ter entre 3 a 60 caracteres.")
    private String name;

    @NotNull(message = "Documento não pode ser vazio ou nulo.")
    @ValidCNPJ(message = "Já existe um cadastro com esse CNPJ.")
    @CNPJ(message = "Documento com formato inválido.")
    private String document;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", locale = "en-US", timezone = "Brazil/East")
    private LocalDateTime createdDate;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", locale = "en-US", timezone = "Brazil/East")
    private LocalDateTime updatedDate;

    private UUID externalCode;

    public Partner convertDTOToEntity() {
        return new ModelMapper().map(this, Partner.class);
    }
}
