package io.github.olszewskif.fridge.catalog.application.service;

import io.github.olszewskif.fridge.catalog.application.dto.CategoryResult;
import io.github.olszewskif.fridge.catalog.application.dto.CreateCategoryCommand;
import io.github.olszewskif.fridge.catalog.application.dto.UpdateCategoryCommand;
import io.github.olszewskif.fridge.catalog.application.mapper.CategoryAppMapper;
import io.github.olszewskif.fridge.catalog.application.usecase.CategoryUseCase;
import io.github.olszewskif.fridge.catalog.domain.Category;
import io.github.olszewskif.fridge.catalog.domain.CategoryId;
import io.github.olszewskif.fridge.catalog.domain.exception.CategoryNotFoundException;
import io.github.olszewskif.fridge.catalog.domain.port.CategoryRepository;
import io.github.olszewskif.fridge.shared.domain.PageQuery;
import io.github.olszewskif.fridge.shared.domain.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService implements CategoryUseCase {

    private final CategoryRepository repository;
    private final CategoryAppMapper mapper;

    @Transactional(readOnly = true)
    @Override
    public CategoryResult getCategory(CategoryId id) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        return mapper.toResult(category);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResult<CategoryResult> getCategories(PageQuery pageQuery) {
        return repository.findAll(pageQuery).map(mapper::toResult);
    }

    @Override
    public CategoryResult createCategory(CreateCategoryCommand command) {
        Category category = Category.create(command.name(), resolveParent(command.parentId()));
        return mapper.toResult(repository.save(category));
    }

    @Override
    public CategoryResult updateCategory(CategoryId id, UpdateCategoryCommand command) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        if (command.name() != null) category.updateName(command.name());
        if (command.parentId() != null) category.updateParent(resolveParent(command.parentId()));
        return mapper.toResult(repository.save(category));
    }

    @Override
    public void deleteCategory(CategoryId id) {
        if (!repository.existsById(id)) {
            throw new CategoryNotFoundException(id);
        }
        repository.delete(id);
    }

    private CategoryId resolveParent(CategoryId parentId) {
        if (parentId == null) return null;
        if (!repository.existsById(parentId)) {
            throw new CategoryNotFoundException(parentId);
        }
        return parentId;
    }

}
