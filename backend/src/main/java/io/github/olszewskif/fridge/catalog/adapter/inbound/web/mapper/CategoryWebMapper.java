package io.github.olszewskif.fridge.catalog.adapter.inbound.web.mapper;

import io.github.olszewskif.fridge.catalog.adapter.inbound.web.dto.CategoryResponse;
import io.github.olszewskif.fridge.catalog.adapter.inbound.web.dto.CreateCategoryRequest;
import io.github.olszewskif.fridge.catalog.adapter.inbound.web.dto.UpdateCategoryRequest;
import io.github.olszewskif.fridge.catalog.application.dto.CategoryResult;
import io.github.olszewskif.fridge.catalog.application.dto.CreateCategoryCommand;
import io.github.olszewskif.fridge.catalog.application.dto.UpdateCategoryCommand;
import io.github.olszewskif.fridge.catalog.domain.CategoryId;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public final class CategoryWebMapper {

    public CategoryResponse toResponse(CategoryResult result) {
        return new CategoryResponse(
                result.id().getValue(),
                result.name(),
                result.parentId() == null ? null : result.parentId().getValue()
        );
    }

    public CreateCategoryCommand toCommand(CreateCategoryRequest request) {
        return new CreateCategoryCommand(
                request.name(),
                toCategoryId(request.parentId())
        );
    }

    public UpdateCategoryCommand toCommand(UpdateCategoryRequest request) {
        return new UpdateCategoryCommand(
                request.name(),
                toCategoryId(request.parentId())
        );
    }

    private static CategoryId toCategoryId(UUID id) {
        return id == null ? null : CategoryId.of(id);
    }
}
