package io.github.olszewskif.fridge.inventory.adapter.inbound.web.mapper;

import io.github.olszewskif.fridge.inventory.adapter.inbound.web.dto.*;
import io.github.olszewskif.fridge.inventory.application.dto.*;
import io.github.olszewskif.fridge.inventory.domain.*;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public final class FridgeItemWebMapper {

    public FridgeItemResponse toResponse(FridgeItemResult result) {
        return new FridgeItemResponse(
                result.id().getValue(),
                result.productId().getValue(),
                result.addedBy().getValue(),
                result.addedAt(),
                result.quantity().value(),
                result.expiryDate() == null ? null : result.expiryDate().value()
        );
    }

    public AddFridgeItemCommand toCommand(UUID fridgeId, AddFridgeItemRequest request) {
        return new AddFridgeItemCommand(
                FridgeId.of(fridgeId),
                request.productId() == null ? null : ProductId.of(request.productId()),
                new Quantity(request.quantity()),
                request.expiryDate() == null ? null : new ExpiryDate(request.expiryDate())
        );
    }

    public UpdateFridgeItemCommand toCommand(UUID fridgeId, UUID itemId, UpdateFridgeItemRequest request) {
        return new UpdateFridgeItemCommand(
                FridgeId.of(fridgeId),
                FridgeItemId.of(itemId),
                request.quantity() == null ? null : new Quantity(request.quantity()),
                request.expiryDate() == null ? null : new ExpiryDate(request.expiryDate())
        );
    }

    public FridgeItemsFilterQuery toFilterQuery(UUID id, FridgeItemsFilterRequest request) {
        return new FridgeItemsFilterQuery(
                id == null ? null : FridgeId.of(id),
                request.isExpired(),
                request.expiresInDays(),
                request.productId() == null ? null : ProductId.of(request.productId()),
                request.categoryIds() == null ? null : request.categoryIds().stream()
                        .map(CategoryId::of).collect(Collectors.toSet()),
                request.addedBy() == null ? null : UserId.of(request.addedBy()),
                request.search()
        );
    }
}
