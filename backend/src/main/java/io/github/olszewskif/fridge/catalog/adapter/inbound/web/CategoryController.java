package io.github.olszewskif.fridge.catalog.adapter.inbound.web;

import io.github.olszewskif.fridge.catalog.adapter.inbound.web.dto.CategoryFilterRequest;
import io.github.olszewskif.fridge.catalog.adapter.inbound.web.dto.CategoryResponse;
import io.github.olszewskif.fridge.catalog.adapter.inbound.web.dto.CreateCategoryRequest;
import io.github.olszewskif.fridge.catalog.adapter.inbound.web.dto.UpdateCategoryRequest;
import io.github.olszewskif.fridge.catalog.adapter.inbound.web.mapper.CategoryWebMapper;
import io.github.olszewskif.fridge.catalog.application.usecase.CategoryUseCase;
import io.github.olszewskif.fridge.catalog.domain.CategoryFilterQuery;
import io.github.olszewskif.fridge.catalog.domain.CategoryId;
import io.github.olszewskif.fridge.shared.adapter.inbound.web.mapper.CommonWebMapper;
import io.github.olszewskif.fridge.shared.domain.PageQuery;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryUseCase useCase;
    private final CategoryWebMapper mapper;
    private final CommonWebMapper commonWebMapper;

    @GetMapping("/{id}")
    public CategoryResponse getCategory(@PathVariable UUID id) {
        return mapper.toResponse(useCase.getCategory(CategoryId.of(id)));
    }

    @GetMapping
    public Page<CategoryResponse> getCategories(
            CategoryFilterRequest request,
            Pageable pageable
    ) {
        PageQuery pageQuery = commonWebMapper.toPageQuery(pageable);
        CategoryFilterQuery filterQuery = mapper.toFilterQuery(request);
        var result = useCase.getCategories(filterQuery, pageQuery).map(mapper::toResponse);
        return new PageImpl<>(result.data(), pageable, result.totalElements());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        return mapper.toResponse(useCase.createCategory(mapper.toCommand(request)));
    }

    @PatchMapping("/{id}")
    public CategoryResponse updateCategory(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCategoryRequest request
    ) {
        return mapper.toResponse(useCase.updateCategory(CategoryId.of(id), mapper.toCommand(request)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable UUID id) {
        useCase.deleteCategory(CategoryId.of(id));
    }

}
