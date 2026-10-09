package io.github.olszewskif.fridge.catalog.application.mapper;

import io.github.olszewskif.fridge.catalog.application.dto.CategoryResult;
import io.github.olszewskif.fridge.catalog.domain.Category;
import org.springframework.stereotype.Component;

@Component
public final class CategoryAppMapper {

    public CategoryResult toResult(Category category) {
        return new CategoryResult(
                category.getId(),
                category.getName(),
                category.getParentId()
        );
    }
}
