package io.github.olszewskif.fridge.inventory.adapter.inbound.web.mapper;

import io.github.olszewskif.fridge.inventory.adapter.inbound.web.dto.*;
import io.github.olszewskif.fridge.inventory.application.dto.CreateFridgeCommand;
import io.github.olszewskif.fridge.inventory.application.dto.FridgeItemResult;
import io.github.olszewskif.fridge.inventory.application.dto.FridgeResult;
import io.github.olszewskif.fridge.inventory.application.dto.UpdateFridgeCommand;
import org.springframework.stereotype.Component;

@Component
public final class FridgeItemWebMapper {

    public FridgeItemResponse toResponse(FridgeItemResult result) {
        return new FridgeItemResponse(
                result.id().getValue(),
                result.productId().getValue(),
                result.addedBy().getValue(),
                result.addedAt(),
                new AmountDto(result.amount().quantity(), result.amount().unit().getName()),
                result.expiryDate() == null ? null : result.expiryDate().value()
        );
    }

    public CreateFridgeCommand toCommand(CreateFridgeRequest request) {
        return new CreateFridgeCommand(
                request.name()
        );
    }

    public UpdateFridgeCommand toCommand(UpdateFridgeRequest request) {
        return new UpdateFridgeCommand(
                request.name()
        );
    }
}
