package io.github.olszewskif.fridge.catalog.adapter.outbound.persistence.mapper;

import io.github.olszewskif.fridge.catalog.adapter.outbound.persistence.ProductJpaEntity;
import io.github.olszewskif.fridge.catalog.domain.*;
import io.github.olszewskif.fridge.shared.domain.DomainId;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public final class ProductJpaMapper {

    public ProductJpaEntity toJpaEntity(Product product) {
        ProductJpaEntity entity = new ProductJpaEntity();
        entity.setId(product.getId().getValue());
        updateJpaEntity(product, entity);
        return entity;
    }

    public void updateJpaEntity(Product product, ProductJpaEntity entity) {
        entity.setName(product.getName());
        entity.setBrand(product.getBrand());
        entity.setBarcode(product.getBarcode().value());
        NetQuantity netQuantity = product.getNetQuantity();
        Double amount = netQuantity == null ? null : netQuantity.amount();
        String unit = netQuantity == null ? null : netQuantity.unit().getDisplayName();
        entity.setAmount(amount);
        entity.setUnit(unit);
        entity.setImageUrl(product.getImageUrl());
        entity.setCategoryIds(mapToUuid(product.getCategories()));
    }

    public Product toDomainEntity(ProductJpaEntity entity) {
        return Product.restore(
                ProductId.of(entity.getId()),
                entity.getName(),
                entity.getBrand(),
                new Barcode(entity.getBarcode()),
                toNetQuantity(entity.getAmount(), entity.getUnit()),
                entity.getImageUrl(),
                mapToCategoryIds(entity.getCategoryIds())
        );
    }

    private static NetQuantity toNetQuantity(Double amount, String unit) {
        if (amount == null || amount <= 0 || unit == null || unit.isBlank()) {
            return null;
        }
        return new NetQuantity(amount, Unit.fromString(unit));
    }

    private static Set<CategoryId> mapToCategoryIds(Set<UUID> uuids) {
        if (uuids == null) return new HashSet<>();
        return uuids.stream().map(CategoryId::of)
                .collect(Collectors.toSet());
    }

    private static Set<UUID> mapToUuid(Set<CategoryId> categoryIds) {
        return categoryIds.stream().map(DomainId::getValue)
                .collect(Collectors.toSet());
    }
}
