package io.github.olszewskif.fridge.inventory.adapter.inbound.web;

import io.github.olszewskif.fridge.inventory.adapter.inbound.web.dto.*;
import io.github.olszewskif.fridge.inventory.adapter.inbound.web.mapper.FridgeItemWebMapper;
import io.github.olszewskif.fridge.inventory.adapter.inbound.web.mapper.FridgeWebMapper;
import io.github.olszewskif.fridge.inventory.application.dto.*;
import io.github.olszewskif.fridge.inventory.application.usecase.FridgeItemCommandUseCase;
import io.github.olszewskif.fridge.inventory.application.usecase.FridgeItemQueryUseCase;
import io.github.olszewskif.fridge.inventory.application.usecase.FridgeUseCase;
import io.github.olszewskif.fridge.inventory.domain.*;
import io.github.olszewskif.fridge.shared.adapter.inbound.web.mapper.CommonWebMapper;
import io.github.olszewskif.fridge.shared.domain.PageQuery;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fridges")
public class FridgeController {

    private final FridgeUseCase useCase;
    private final FridgeItemQueryUseCase fridgeItemQueryUseCase;
    private final FridgeItemCommandUseCase fridgeItemCommandUseCase;
    private final FridgeWebMapper fridgeMapper;
    private final FridgeItemWebMapper fridgeItemMapper;
    private final CommonWebMapper commonWebMapper;

    @GetMapping("/{id}")
    public FridgeResponse getFridge(@PathVariable UUID id) {
        return fridgeMapper.toResponse(useCase.getFridge(FridgeId.of(id)));
    }

    @GetMapping
    public Page<FridgeResponse> getFridges(
            FridgeFilterRequest request,
            Pageable pageable
    ) {
        var filter = fridgeMapper.toFilterQuery(request);
        var result = useCase.getFridges(filter, commonWebMapper.toPageQuery(pageable)).map(fridgeMapper::toResponse);
        return commonWebMapper.toPage(result, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FridgeResponse createFridge(@Valid @RequestBody CreateFridgeRequest request) {
        return fridgeMapper.toResponse(useCase.createFridge(fridgeMapper.toCommand(request)));
    }

    @PatchMapping("/{id}")
    public FridgeResponse updateFridge(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateFridgeRequest request
    ) {
        return fridgeMapper.toResponse(useCase.updateFridge(FridgeId.of(id), fridgeMapper.toCommand(request)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFridge(@PathVariable UUID id) {
        useCase.deleteFridge(FridgeId.of(id));
    }


    @GetMapping("/{id}/items/{itemId}")
    public FridgeItemResponse getItem(@PathVariable UUID id, @PathVariable UUID itemId) {
        var query = new GetFridgeItemQuery(FridgeItemId.of(itemId));
        return fridgeItemMapper.toResponse(fridgeItemQueryUseCase.getItem(query));
    }

    @GetMapping("/{id}/items")
    public Page<FridgeItemResponse> getItems(@PathVariable UUID id, FridgeItemsFilterRequest request,
                                             Pageable pageable) {
        PageQuery pageQuery = commonWebMapper.toPageQuery(pageable);
        FridgeItemsFilterQuery filters = fridgeItemMapper.toFilterQuery(id, request);
        var pageRes = fridgeItemQueryUseCase.getItems(filters, pageQuery)
                .map(fridgeItemMapper::toResponse);
        return commonWebMapper.toPage(pageRes, pageable);
    }

    @PostMapping("/{id}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public FridgeItemResponse addItem(@PathVariable UUID id,
                                      @RequestBody @Valid AddFridgeItemRequest request) {
        var cmd = fridgeItemMapper.toCommand(id, request);
        return fridgeItemMapper.toResponse(fridgeItemCommandUseCase.addItem(cmd));
    }

    @PatchMapping("/{id}/items/{itemId}")
    public FridgeItemResponse updateItem(@PathVariable UUID id, @PathVariable UUID itemId,
                                         @RequestBody @Valid UpdateFridgeItemRequest request) {
        var cmd = fridgeItemMapper.toCommand(id, itemId, request);
        return fridgeItemMapper.toResponse(fridgeItemCommandUseCase.updateItem(cmd));
    }

    @DeleteMapping("/{id}/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItem(@PathVariable UUID id, @PathVariable UUID itemId) {
        var cmd = new RemoveFridgeItemCommand(FridgeId.of(id), FridgeItemId.of(itemId));
        fridgeItemCommandUseCase.removeItem(cmd);
    }

    @PostMapping("/{id}/items/bulk-delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItems(@PathVariable("id") UUID fridgeId,
                            @RequestBody @Valid BulkRemoveFridgeItemsRequest request) {
        var cmd = fridgeItemMapper.toBulkCommand(fridgeId, request);
        fridgeItemCommandUseCase.bulkRemoveItems(cmd);
    }


}
