package io.github.olszewskif.fridge.catalog.application.usecase;

import io.github.olszewskif.fridge.catalog.application.dto.CategoryResult;
import io.github.olszewskif.fridge.catalog.application.dto.CreateCategoryCommand;
import io.github.olszewskif.fridge.catalog.application.dto.UpdateCategoryCommand;
import io.github.olszewskif.fridge.catalog.domain.CategoryId;
import io.github.olszewskif.fridge.shared.domain.PageQuery;
import io.github.olszewskif.fridge.shared.domain.PageResult;

public interface CategoryUseCase {
    CategoryResult getCategory(CategoryId id);
    PageResult<CategoryResult> getCategories(PageQuery pageQuery);
    CategoryResult createCategory(CreateCategoryCommand command);
    CategoryResult updateCategory(CategoryId id, UpdateCategoryCommand command);
    void deleteCategory(CategoryId id);
}
