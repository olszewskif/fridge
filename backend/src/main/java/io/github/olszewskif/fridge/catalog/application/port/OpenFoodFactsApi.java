package io.github.olszewskif.fridge.catalog.application.port;

import io.github.olszewskif.fridge.catalog.application.dto.ExternalProduct;
import io.github.olszewskif.fridge.catalog.domain.Barcode;

import java.util.Optional;

public interface OpenFoodFactsApi {
    Optional<ExternalProduct> findByBarcode(Barcode barcode);
}
