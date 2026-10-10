package io.github.olszewskif.fridge.inventory.adapter.inbound.web.mapper;

import io.github.olszewskif.fridge.inventory.adapter.inbound.web.dto.CreateFridgeRequest;
import io.github.olszewskif.fridge.inventory.adapter.inbound.web.dto.FridgeResponse;
import io.github.olszewskif.fridge.inventory.adapter.inbound.web.dto.UpdateFridgeRequest;
import io.github.olszewskif.fridge.inventory.application.dto.CreateFridgeCommand;
import io.github.olszewskif.fridge.inventory.application.dto.FridgeResult;
import io.github.olszewskif.fridge.inventory.application.dto.UpdateFridgeCommand;
import org.springframework.stereotype.Component;

@Component
public final class FridgeWebMapper {

    public FridgeResponse toResponse(FridgeResult result) {
        return new FridgeResponse(
                result.id().getValue(),
                result.name(),
                result.createdAt(),
                result.createdBy().getValue()
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
