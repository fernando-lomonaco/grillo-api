package br.com.grillo.model;

import br.com.grillo.model.entity.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "categories")
public class CategoryModel extends RepresentationModel<CategoryModel> {

    private Long code;
    @NotNull(message = "Nome não pode ser vazio ou nulo")
    @Size(min = 3, max = 40, message = "Nome deve ter entre 3 a 40 caracteres")
    private String name;
    @NotNull(message = "Descrição não pode ser vazio ou nulo")
    @Size(min = 3, max = 80, message = "Descrição deve ter entre 3 a 80 caracteres")
    private String description;
    @NotNull(message = "Status não pode ser vazio ou nulo")
    private String status;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", locale = "en-US", timezone = "Brazil/East")
    private LocalDateTime createdDate;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", locale = "en-US", timezone = "Brazil/East")
    private LocalDateTime updatedDate;

    public Category convertDTOToEntity() {
        return new ModelMapper().map(this, Category.class);
    }

}
