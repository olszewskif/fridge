package io.github.olszewskif.fridge.inventory.application.port;

import io.github.olszewskif.fridge.inventory.application.dto.ResolveProductsCommand;
import io.github.olszewskif.fridge.inventory.application.dto.ResolveProductsResult;

public interface ProductResolver {
    ResolveProductsResult resolveProducts(ResolveProductsCommand command);
}
