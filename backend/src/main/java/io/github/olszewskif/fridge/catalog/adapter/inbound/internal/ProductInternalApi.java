package io.github.olszewskif.fridge.catalog.adapter.inbound.internal;

import io.github.olszewskif.fridge.catalog.adapter.inbound.internal.dto.ResolveProductsRequest;
import io.github.olszewskif.fridge.catalog.adapter.inbound.internal.dto.ResolveProductsResponse;

public interface ProductInternalApi {
    ResolveProductsResponse resolveProducts(ResolveProductsRequest request);
}
