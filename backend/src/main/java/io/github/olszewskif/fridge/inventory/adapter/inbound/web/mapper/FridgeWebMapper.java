package io.github.olszewskif.fridge.inventory.adapter.inbound.web.mapper;

import io.github.olszewskif.fridge.inventory.adapter.inbound.web.dto.CreateFridgeRequest;
import io.github.olszewskif.fridge.inventory.adapter.inbound.web.dto.FridgeFilterRequest;
import io.github.olszewskif.fridge.inventory.adapter.inbound.web.dto.FridgeResponse;
import io.github.olszewskif.fridge.inventory.adapter.inbound.web.dto.UpdateFridgeRequest;
import io.github.olszewskif.fridge.inventory.application.dto.CreateFridgeCommand;
import io.github.olszewskif.fridge.inventory.application.dto.FridgeResult;
import io.github.olszewskif.fridge.inventory.application.dto.UpdateFridgeCommand;
import io.github.olszewskif.fridge.inventory.domain.FridgeFilterQuery;
import io.github.olszewskif.fridge.inventory.domain.UserId;
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

    public FridgeFilterQuery toFilterQuery(FridgeFilterRequest request) {
        return new FridgeFilterQuery(
                request.search(),
                request.ownerId() == null ? null : UserId.of(request.ownerId())
        );
    }
}
