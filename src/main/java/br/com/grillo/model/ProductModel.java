package br.com.grillo.model;

import br.com.grillo.model.entity.Product;
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
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "products")
public class ProductModel extends RepresentationModel<ProductModel> {

    private UUID code;
    @NotNull(message = "Nome não pode ser vazio ou nulo")
    @Size(min = 3, max = 30, message = "Nome deve ter entre 3 a 30 caracteres")
    private String name;
    @NotNull(message = "Descricão não pode ser vazio ou nulo")
    @Size(min = 3, max = 80, message = "Descrição deve ter entre 3 a 80 caracteres")
    private String description;
    @NotNull(message = "Status não pode ser vazio ou nulo")
    private String status;
    @NotNull(message = "Categoria não pode ser vazio ou nulo")
    private CategoryModel category;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", locale = "en-US", timezone = "Brazil/East")
    private LocalDateTime createdDate;
    private String updatedDate;

    public Product convertDTOToEntity() {
        return new ModelMapper().map(this, Product.class);
    }
}
