package io.github.olszewskif.fridge.catalog.adapter.inbound.internal;

import io.github.olszewskif.fridge.catalog.adapter.inbound.internal.dto.*;

public interface ProductInternalApi {
    SearchProductsResponse searchProducts(SearchProductsRequest request);
    ResolveProductResponse resolveProduct(ResolveProductRequest request);
    ResolveProductsBatchResponse resolveProductsBatch(ResolveProductsBatchRequest request);
}
