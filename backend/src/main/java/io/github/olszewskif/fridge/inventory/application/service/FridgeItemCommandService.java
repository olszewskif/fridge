package io.github.olszewskif.fridge.inventory.application.service;

import io.github.olszewskif.fridge.inventory.application.dto.*;
import io.github.olszewskif.fridge.inventory.application.mapper.FridgeItemAppMapper;
import io.github.olszewskif.fridge.inventory.application.usecase.FridgeItemCommandUseCase;
import io.github.olszewskif.fridge.inventory.domain.Fridge;
import io.github.olszewskif.fridge.inventory.domain.FridgeId;
import io.github.olszewskif.fridge.inventory.domain.FridgeItem;
import io.github.olszewskif.fridge.inventory.domain.UserId;
import io.github.olszewskif.fridge.inventory.domain.exception.FridgeNotFoundException;
import io.github.olszewskif.fridge.inventory.domain.port.FridgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FridgeItemCommandService implements FridgeItemCommandUseCase {

    private final FridgeRepository fridgeRepository;
    private final FridgeItemAppMapper itemMapper;

    @Override
    public FridgeItemResult addItem(AddFridgeItemCommand command) {
        Fridge fridge = getFridgeById(command.fridgeId());
        UserId mockUserId = UserId.generate();

        FridgeItem item = fridge.addItem(command.productId(), mockUserId, command.quantity(), command.expiryDate());
        fridgeRepository.save(fridge);

        return itemMapper.toResult(item);
    }

    @Override
    public FridgeItemResult updateItem(UpdateFridgeItemCommand command) {
        Fridge fridge = getFridgeById(command.fridgeId());
        if (command.quantity() != null) fridge.changeItemQuantity(command.itemId(), command.quantity());
        if (command.expiryDate() != null) fridge.changeItemExpiryDate(command.itemId(), command.expiryDate());
        fridgeRepository.save(fridge);
        return itemMapper.toResult(fridge.getItem(command.itemId()));
    }

    @Override
    public void removeItem(RemoveFridgeItemCommand command) {
        Fridge fridge = getFridgeById(command.fridgeId());
        fridge.removeItem(command.fridgeItemId());
        fridgeRepository.save(fridge);
    }

    private Fridge getFridgeById(FridgeId id) {
        return fridgeRepository.findById(id)
                .orElseThrow(() -> new FridgeNotFoundException(id));
    }
}
