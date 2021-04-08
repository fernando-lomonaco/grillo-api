package br.com.grillo.dto.resource;

import br.com.grillo.controller.CategoryController;
import br.com.grillo.dto.CategoryDTO;
import br.com.grillo.model.Category;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class CategoryModelAssembler extends RepresentationModelAssemblerSupport<Category, CategoryDTO> {

    public CategoryModelAssembler() {
        super(CategoryController.class, CategoryDTO.class);
    }

    @Override
    public CategoryDTO toModel(Category category) {
        CategoryDTO categoryDTO = buildCategoryModel(category);
        categoryDTO.add((WebMvcLinkBuilder.linkTo(CategoryController.class).slash(category.getCode())).withSelfRel());
        return categoryDTO;
    }

    private CategoryDTO buildCategoryModel(Category category) {
        return CategoryDTO.builder()
                .code(category.getCode())
                .name(category.getName())
                .description(category.getDescription())
                .status(String.valueOf(category.getStatus()))
                .externalCode(category.getExternalCode())
                .createdDate(category.getCreatedDate())
                .updatedDate(category.getUpdatedDate())
                .build();
    }
}
