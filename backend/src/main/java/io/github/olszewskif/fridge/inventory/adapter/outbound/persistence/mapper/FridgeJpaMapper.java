package io.github.olszewskif.fridge.inventory.adapter.outbound.persistence.mapper;

import io.github.olszewskif.fridge.inventory.adapter.outbound.persistence.FridgeItemJpaEntity;
import io.github.olszewskif.fridge.inventory.adapter.outbound.persistence.FridgeJpaEntity;
import io.github.olszewskif.fridge.inventory.domain.Fridge;
import io.github.olszewskif.fridge.inventory.domain.FridgeId;
import io.github.olszewskif.fridge.inventory.domain.FridgeItem;
import io.github.olszewskif.fridge.inventory.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public final class FridgeJpaMapper {

    private final FridgeItemJpaMapper itemMapper;

    public FridgeJpaEntity toJpaEntity(Fridge fridge) {
        FridgeJpaEntity entity = new FridgeJpaEntity();
        entity.setId(fridge.getId().getValue());
        updateJpaEntity(fridge, entity);
        return entity;
    }

    public void updateJpaEntity(Fridge fridge, FridgeJpaEntity entity) {
        entity.setName(fridge.getName());
        entity.setCreatedBy(fridge.getCreatedBy().getValue());
        if (fridge.getCreatedAt() != null) {
            entity.setCreatedAt(fridge.getCreatedAt());
        }

        List<FridgeItem> fridgeItems = fridge.getItems();

        entity.getItems().removeIf(jpaItem -> fridgeItems.stream()
                .noneMatch(domItem ->
                        domItem.getId().getValue().equals(jpaItem.getId())));

        fridgeItems.forEach(domItem -> {
            entity.getItems().stream()
                    .filter(jpaItem ->
                            jpaItem.getId().equals(domItem.getId().getValue()))
                    .findFirst()
                    .ifPresentOrElse(
                            jpaItem -> itemMapper.updateJpaEntity(domItem, jpaItem),
                            () -> entity.getItems().add(itemMapper.toJpaEntity(domItem)));
        });
    }

    public Fridge toDomainEntity(FridgeJpaEntity entity) {
        return Fridge.restore(
                FridgeId.of(entity.getId()),
                entity.getName(),
                entity.getCreatedAt(),
                UserId.of(entity.getCreatedBy()),
                entity.getItems().stream()
                        .map(itemMapper::toDomain).toList()
        );
    }
}
