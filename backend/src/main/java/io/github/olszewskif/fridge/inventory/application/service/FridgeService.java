package io.github.olszewskif.fridge.inventory.application.service;

import io.github.olszewskif.fridge.inventory.application.dto.*;
import io.github.olszewskif.fridge.inventory.application.mapper.FridgeAppMapper;
import io.github.olszewskif.fridge.inventory.application.usecase.FridgeUseCase;
import io.github.olszewskif.fridge.inventory.domain.Fridge;
import io.github.olszewskif.fridge.inventory.domain.FridgeFilterQuery;
import io.github.olszewskif.fridge.inventory.domain.FridgeId;
import io.github.olszewskif.fridge.inventory.domain.UserId;
import io.github.olszewskif.fridge.inventory.domain.exception.FridgeNotFoundException;
import io.github.olszewskif.fridge.inventory.domain.port.FridgeRepository;
import io.github.olszewskif.fridge.shared.domain.PageQuery;
import io.github.olszewskif.fridge.shared.domain.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class FridgeService implements FridgeUseCase {

    private final FridgeRepository repository;
    private final FridgeAppMapper mapper;

    @Transactional(readOnly = true)
    @Override
    public FridgeResult getFridge(FridgeId id) {
        Fridge fridge = getFridgeById(id);
        return mapper.toResult(fridge);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResult<FridgeResult> getFridges(FridgeFilterQuery filters,
                                               PageQuery pageQuery) {
        return repository.findAll(filters, pageQuery).map(mapper::toResult);
    }

    @Override
    public FridgeResult createFridge(CreateFridgeCommand command) {
        // Mocking user id for now as requested
        UserId currentUserId = UserId.of(UUID.randomUUID());
        Fridge fridge = Fridge.create(command.name(), currentUserId);
        return mapper.toResult(repository.save(fridge));
    }

    @Override
    public FridgeResult updateFridge(FridgeId id, UpdateFridgeCommand command) {
        Fridge fridge = getFridgeById(id);
        if (command.name() != null) fridge.changeName(command.name());
        return mapper.toResult(repository.save(fridge));
    }

    @Override
    public void deleteFridge(FridgeId id) {
        if (!repository.existsById(id)) {
            throw new FridgeNotFoundException(id);
        }
        repository.delete(id);
    }

    @Override
    public void joinFridge(JoinFridgeCommand command) {
        Fridge fridge = getFridgeById(command.fridgeId());
        fridge.addUser(command.userId());
        repository.save(fridge);
    }

    @Override
    public void leaveFridge(LeaveFridgeCommand command) {
        Fridge fridge = getFridgeById(command.fridgeId());
        fridge.removeUser(command.userId());
        repository.save(fridge);
    }

    private Fridge getFridgeById(FridgeId id) {
        return repository.findById(id)
                .orElseThrow(() -> new FridgeNotFoundException(id));
    }
}
