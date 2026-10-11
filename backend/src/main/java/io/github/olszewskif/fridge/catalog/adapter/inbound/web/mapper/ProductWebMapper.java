package io.github.olszewskif.fridge.catalog.adapter.inbound.web.mapper;

import io.github.olszewskif.fridge.catalog.adapter.inbound.web.dto.*;
import io.github.olszewskif.fridge.catalog.application.dto.CreateProductCommand;
import io.github.olszewskif.fridge.catalog.application.dto.ProductResult;
import io.github.olszewskif.fridge.catalog.application.dto.UpdateProductCommand;
import io.github.olszewskif.fridge.catalog.domain.*;
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
                toNetQuantityDto(result.netQuantity()),
                result.imageUrl(),
                mapToUUID(result.categories())
        );
    }

    public CreateProductCommand toCommand(CreateProductRequest request) {
        return new CreateProductCommand(
                request.name(),
                request.brand(),
                toBarcode(request.barcode()),
                toNetQuantity(request.netQuantity()),
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
                toNetQuantity(request.netQuantity()),
                request.imageUrl()
        );
    }

    public ProductFilterQuery toFilterQuery(ProductFilterRequest request) {
        return new ProductFilterQuery(request.search(),
                mapToCategoryId(request.categoryIds()));
    }

    private static NetQuantity toNetQuantity(NetQuantityDto dto) {
        if (dto == null) return null;
        return new NetQuantity(dto.amount(), Unit.fromString(dto.unit()));
    }

    private static NetQuantityDto toNetQuantityDto(NetQuantity netQuantity) {
        if (netQuantity == null) return null;
        return new NetQuantityDto(netQuantity.amount(), netQuantity.unit().getDisplayName());
    }

    private static Barcode toBarcode(String value) {
        return value == null ? null : new Barcode(value);
    }

    private static Set<ProductResponse.CategorySummary> mapToUUID(Set<ProductResult.CategorySummary> categories) {
        if (categories == null) return null;
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
