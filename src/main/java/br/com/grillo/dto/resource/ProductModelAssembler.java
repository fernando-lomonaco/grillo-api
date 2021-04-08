package br.com.grillo.model.resource;

import br.com.grillo.controller.CategoryController;
import br.com.grillo.controller.ProductController;
import br.com.grillo.model.CategoryModel;
import br.com.grillo.model.ProductModel;
import br.com.grillo.model.entity.Category;
import br.com.grillo.model.entity.Product;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductModelAssembler extends RepresentationModelAssemblerSupport<Product, ProductModel> {

    public ProductModelAssembler() {
        super(ProductController.class, ProductModel.class);
    }

    @Override
    public ProductModel toModel(Product product) {
        ProductModel productModel = createModelWithId(product.getCode(), product);
        productModel.setCode(product.getCode());
        productModel.setName(product.getName());
        productModel.setDescription(product.getDescription());
        productModel.setStatus(String.valueOf(product.getStatus()));
        productModel.setCreatedDate(product.getCreatedDate());
        productModel.setUpdatedDate(String.valueOf(product.getUpdatedDate()));
        productModel.setCategory(toCategoryModel(product.getCategory()));

        productModel.add(linkTo(methodOn(ProductController.class).all(0, 20, product.getCategory().getCode(), "A", null)).withRel("products"));
        productModel.add(linkTo(methodOn(CategoryController.class).get(product.getCategory().getCode()))
                .withRel("category"));

        return productModel;
    }

    private CategoryModel toCategoryModel(Category category) {
        return CategoryModel.builder()
                .code(category.getCode())
                .name(category.getName())
                .build();
    }

}
