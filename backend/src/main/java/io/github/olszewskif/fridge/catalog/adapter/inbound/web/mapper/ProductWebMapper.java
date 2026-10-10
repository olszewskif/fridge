package io.github.olszewskif.fridge.catalog.adapter.inbound.web.mapper;

import io.github.olszewskif.fridge.catalog.adapter.inbound.web.dto.CreateProductRequest;
import io.github.olszewskif.fridge.catalog.adapter.inbound.web.dto.ProductFilterRequest;
import io.github.olszewskif.fridge.catalog.adapter.inbound.web.dto.ProductResponse;
import io.github.olszewskif.fridge.catalog.adapter.inbound.web.dto.UpdateProductRequest;
import io.github.olszewskif.fridge.catalog.application.dto.CreateProductCommand;
import io.github.olszewskif.fridge.catalog.application.dto.ProductResult;
import io.github.olszewskif.fridge.catalog.application.dto.UpdateProductCommand;
import io.github.olszewskif.fridge.catalog.domain.Barcode;
import io.github.olszewskif.fridge.catalog.domain.CategoryId;
import io.github.olszewskif.fridge.catalog.domain.ProductFilterQuery;
import io.github.olszewskif.fridge.catalog.domain.ProductId;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public final class ProductWebMapper {

    public ProductResponse toResponse(ProductResult result) {
        return new ProductResponse(
                result.id().getValue(),
                result.name(),
                result.brand(),
                result.barcode().value(),
                result.imageUrl(),
                mapToUUID(result.categories())
        );
    }

    public CreateProductCommand toCommand(CreateProductRequest request) {
        return new CreateProductCommand(
                request.name(),
                request.brand(),
                toBarcode(request.barcode()),
                request.imageUrl(),
                mapToCategoryId(request.categoryIds())
        );
    }

    public UpdateProductCommand toCommand(UUID id, UpdateProductRequest request) {
        return new UpdateProductCommand(
                ProductId.of(id),
                request.name(),
                request.brand(),
                toBarcode(request.barcode()),
                request.imageUrl()
        );
    }

    public ProductFilterQuery toFilterQuery(ProductFilterRequest request) {
        return new ProductFilterQuery(request.search(),
                mapToCategoryId(request.categoryIds()));
    }

    private static Barcode toBarcode(String value) {
        return value == null ? null : new Barcode(value);
    }

    private static Set<ProductResponse.CategorySummary> mapToUUID(Set<ProductResult.CategorySummary> categories) {
        return categories.stream()
                .map(c -> new ProductResponse.CategorySummary(
                        c.id().getValue(),
                        c.name(),
                        c.parentId() == null ? null : c.parentId().getValue()))
                .collect(Collectors.toSet());
    }

    private static Set<CategoryId> mapToCategoryId(Set<UUID> uuids) {
        if (uuids == null) return new HashSet<>();
        return uuids.stream()
                .map(CategoryId::of)
                .collect(Collectors.toSet());
    }
}
