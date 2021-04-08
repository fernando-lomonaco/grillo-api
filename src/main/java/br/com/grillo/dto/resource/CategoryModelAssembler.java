package br.com.grillo.model.resource;

import br.com.grillo.controller.CategoryController;
import br.com.grillo.model.CategoryModel;
import br.com.grillo.model.entity.Category;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class CategoryModelAssembler extends RepresentationModelAssemblerSupport<Category, CategoryModel> {

    public CategoryModelAssembler() {
        super(CategoryController.class, CategoryModel.class);
    }

    @Override
    public CategoryModel toModel(Category category) {
        CategoryModel categoryModel = createModelWithId(category.getCode(), category);
        categoryModel.setCode(category.getCode());
        categoryModel.setName(category.getName());
        categoryModel.setDescription(category.getDescription());
        categoryModel.setStatus(String.valueOf(category.getStatus()));
        categoryModel.setCreatedDate(category.getCreatedDate());
        categoryModel.setUpdatedDate(category.getUpdatedDate());
        categoryModel.setExternalCode(category.getExternalCode());

        return categoryModel;
    }
}
