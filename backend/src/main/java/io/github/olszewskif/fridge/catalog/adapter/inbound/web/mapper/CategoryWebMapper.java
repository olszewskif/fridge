package io.github.olszewskif.fridge.catalog.adapter.inbound.web.mapper;

import io.github.olszewskif.fridge.catalog.adapter.inbound.web.dto.CategoryFilterRequest;
import io.github.olszewskif.fridge.catalog.adapter.inbound.web.dto.CategoryResponse;
import io.github.olszewskif.fridge.catalog.adapter.inbound.web.dto.CreateCategoryRequest;
import io.github.olszewskif.fridge.catalog.adapter.inbound.web.dto.UpdateCategoryRequest;
import io.github.olszewskif.fridge.catalog.application.dto.CategoryResult;
import io.github.olszewskif.fridge.catalog.application.dto.CreateCategoryCommand;
import io.github.olszewskif.fridge.catalog.application.dto.UpdateCategoryCommand;
import io.github.olszewskif.fridge.catalog.domain.Alias;
import io.github.olszewskif.fridge.catalog.domain.CategoryFilterQuery;
import io.github.olszewskif.fridge.catalog.domain.CategoryId;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public final class CategoryWebMapper {

    public CategoryResponse toResponse(CategoryResult result) {
        return new CategoryResponse(
                result.id().getValue(),
                result.name(),
                result.parentId() == null ? null : result.parentId().getValue(),
                result.aliases()
        );
    }

    public CreateCategoryCommand toCommand(CreateCategoryRequest request) {
        Set<Alias> aliases = request.aliases() == null ? Collections.emptySet()
                : request.aliases().stream().map(Alias::of).collect(Collectors.toSet());

        return new CreateCategoryCommand(
                request.name(),
                toCategoryId(request.parentId()),
                aliases
        );
    }

    public UpdateCategoryCommand toCommand(UpdateCategoryRequest request) {
        Set<Alias> aliases = request.aliases() == null ? null
                : request.aliases().stream().map(Alias::of).collect(Collectors.toSet());

        return new UpdateCategoryCommand(
                request.name(),
                toCategoryId(request.parentId()),
                aliases
        );
    }

    public CategoryFilterQuery toFilterQuery(CategoryFilterRequest request) {
        return new CategoryFilterQuery(
                request.search(),
                request.parentId() == null ? null : CategoryId.of(request.parentId()),
                request.isRoot()
        );
    }

    private static CategoryId toCategoryId(UUID id) {
        return id == null ? null : CategoryId.of(id);
    }
}
