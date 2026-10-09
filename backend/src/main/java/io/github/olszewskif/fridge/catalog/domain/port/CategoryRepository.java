package io.github.olszewskif.fridge.catalog.domain.port;

import io.github.olszewskif.fridge.catalog.domain.Category;
import io.github.olszewskif.fridge.catalog.domain.CategoryId;
import io.github.olszewskif.fridge.shared.domain.PageQuery;
import io.github.olszewskif.fridge.shared.domain.PageResult;

import java.util.Optional;
import java.util.Set;

public interface CategoryRepository {
    Optional<Category> findById(CategoryId id);
    boolean existsById(CategoryId id);
    PageResult<Category> findAll(PageQuery pageQuery);
    Set<Category> findAllById(Set<CategoryId> ids);
    Category save(Category category);
    void delete(CategoryId id);
}
