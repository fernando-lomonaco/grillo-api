package br.com.grillo.dto.resource;

import br.com.grillo.controller.CategoryController;
import br.com.grillo.controller.ProductController;
import br.com.grillo.dto.ProductDTO;
import br.com.grillo.model.Product;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductModelAssembler extends RepresentationModelAssemblerSupport<Product, ProductDTO> {

    public ProductModelAssembler() {
        super(ProductController.class, ProductDTO.class);
    }

    @Override
    public ProductDTO toModel(Product product) {
        ProductDTO productDTO = buildProductModel(product);
        productDTO.add(linkTo(methodOn(ProductController.class).all(0, 20)).withRel("products"));
        productDTO.add(linkTo(methodOn(CategoryController.class).get(product.getCategory().getCode()))
                .withRel("category"));
        productDTO.add((WebMvcLinkBuilder.linkTo(ProductController.class).slash(product.getCode())).withSelfRel());

        return productDTO;
    }

    private ProductDTO buildProductModel(Product product) {
        return ProductDTO.builder()
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .status(String.valueOf(product.getStatus()))
                .createdDate(product.getCreatedDate())
                .updatedDate(product.getUpdatedDate())
                .externalCode(product.getExternalCode())
                .categoryCode(product.getCategory().getCode())
                .build();
    }

}
