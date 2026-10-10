package io.github.olszewskif.fridge.inventory.domain.port;

import io.github.olszewskif.fridge.inventory.domain.Fridge;
import io.github.olszewskif.fridge.inventory.domain.FridgeFilterQuery;
import io.github.olszewskif.fridge.inventory.domain.FridgeId;
import io.github.olszewskif.fridge.shared.domain.PageQuery;
import io.github.olszewskif.fridge.shared.domain.PageResult;

import java.util.Optional;

public interface FridgeRepository {
    Optional<Fridge> findById(FridgeId id);
    boolean existsById(FridgeId id);
    PageResult<Fridge> findAll(FridgeFilterQuery filters, PageQuery pageQuery);
    Fridge save(Fridge fridge);
    void delete(FridgeId id);
}
