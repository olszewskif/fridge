package io.github.olszewskif.fridge.inventory.application.port;

import io.github.olszewskif.fridge.inventory.application.dto.*;

public interface ProductResolver {
    SearchProductsResult searchProducts(SearchProductsQuery query);
    ResolveProductResult resolveProduct(ResolveProductQuery query);
    ResolveProductsBatchResult resolveProductsBatch(ResolveProductsBatchQuery query);
}
