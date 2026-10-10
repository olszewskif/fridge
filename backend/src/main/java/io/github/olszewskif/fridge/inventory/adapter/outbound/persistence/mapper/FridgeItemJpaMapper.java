package io.github.olszewskif.fridge.inventory.adapter.outbound.persistence.mapper;

import io.github.olszewskif.fridge.inventory.adapter.outbound.persistence.FridgeItemJpaEntity;
import io.github.olszewskif.fridge.inventory.application.dto.FridgeItemResult;
import io.github.olszewskif.fridge.inventory.domain.*;
import org.springframework.stereotype.Component;

@Component
public final class FridgeItemJpaMapper {

    public FridgeItemResult toResult(FridgeItemJpaEntity e) {
        return new FridgeItemResult(
                FridgeItemId.of(e.getId()),
                ProductId.of(e.getProductId()),
                UserId.of(e.getAddedBy()),
                e.getAddedAt(),
                new Amount(e.getQuantity(), Unit.fromString(e.getUnit())),
                e.getExpiryDate() == null ? null : new ExpiryDate(e.getExpiryDate()));
    }

    public FridgeItemJpaEntity toJpaEntity(FridgeItem item) {
        FridgeItemJpaEntity entity = new FridgeItemJpaEntity();
        entity.setId(item.getId().getValue());
        entity.setProductId(item.getProductId().getValue());
        entity.setAddedBy(item.getAddedBy().getValue());
        entity.setAddedAt(item.getAddedAt());
        entity.setQuantity(item.getAmount().quantity());
        entity.setUnit(item.getAmount().unit().getName());
        entity.setExpiryDate(item.getExpiryDate() != null ? item.getExpiryDate().value() : null);
        return entity;
    }

    public FridgeItem toDomain(FridgeItemJpaEntity entity) {
        return FridgeItem.restore(
                FridgeItemId.of(entity.getId()),
                ProductId.of(entity.getProductId()),
                UserId.of(entity.getAddedBy()),
                entity.getAddedAt(),
                new Amount(entity.getQuantity(), Unit.fromString(entity.getUnit())),
                entity.getExpiryDate() != null ? new ExpiryDate(entity.getExpiryDate()) : null
        );
    }

    public void updateJpaEntity(FridgeItem fridgeItem, FridgeItemJpaEntity jpaEntity) {
        jpaEntity.setQuantity(fridgeItem.getAmount().quantity());
        jpaEntity.setUnit(fridgeItem.getAmount().unit().getName());
        if (fridgeItem.getExpiryDate() != null) {
            jpaEntity.setExpiryDate(fridgeItem.getExpiryDate().value());
        }
    }
}
