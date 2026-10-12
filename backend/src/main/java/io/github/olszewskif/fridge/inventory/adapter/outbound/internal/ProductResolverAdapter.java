package io.github.olszewskif.fridge.inventory.adapter.outbound.internal;

import io.github.olszewskif.fridge.catalog.adapter.inbound.internal.ProductInternalApi;
import io.github.olszewskif.fridge.catalog.adapter.inbound.internal.dto.*;
import io.github.olszewskif.fridge.inventory.application.dto.*;
import io.github.olszewskif.fridge.inventory.application.port.ProductResolver;
import io.github.olszewskif.fridge.inventory.domain.CategoryId;
import io.github.olszewskif.fridge.inventory.domain.ProductId;
import io.github.olszewskif.fridge.shared.domain.DomainId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
class ProductResolverAdapter implements ProductResolver {

    private final ProductInternalApi productInternalApi;

    @Override
    public SearchProductsResult searchProducts(SearchProductsQuery query) {
        Set<UUID> uuids = query.categoryIds() == null ? null
                : query.categoryIds().stream()
                        .map(DomainId::getValue)
                        .collect(Collectors.toSet());
        var request = new SearchProductsRequest(query.search(), uuids);

        SearchProductsResponse response = productInternalApi.searchProducts(request);

        Set<ProductSummaryResult> results = response.products() == null ? Collections.emptySet()
                : response.products().stream()
                        .map(this::toProductSummaryResult)
                        .collect(Collectors.toSet());

        return new SearchProductsResult(results);
    }

    @Override
    public ResolveProductResult resolveProduct(ResolveProductQuery query) {
        if (query.productId() == null) {
            return new ResolveProductResult(null);
        }
        var request = new ResolveProductRequest(query.productId().getValue());
        ResolveProductResponse response = productInternalApi.resolveProduct(request);

        ProductSummaryResult summaryResult = response.product() == null ? null
                : toProductSummaryResult(response.product());

        return new ResolveProductResult(summaryResult);
    }

    @Override
    public ResolveProductsBatchResult resolveProductsBatch(ResolveProductsBatchQuery query) {
        if (query.productIds() == null || query.productIds().isEmpty()) {
            return new ResolveProductsBatchResult(Collections.emptySet());
        }

        Set<UUID> uuids = query.productIds().stream()
                .map(DomainId::getValue)
                .collect(Collectors.toSet());

        var request = new ResolveProductsBatchRequest(uuids);
        ResolveProductsBatchResponse response = productInternalApi.resolveProductsBatch(request);

        Set<ProductSummaryResult> results = response.products() == null ? Collections.emptySet()
                : response.products().stream()
                        .map(this::toProductSummaryResult)
                        .collect(Collectors.toSet());

        return new ResolveProductsBatchResult(results);
    }

    private ProductSummaryResult toProductSummaryResult(ProductSummary p) {
        if (p == null) return null;

        ProductSummaryResult.NetQuantityDto netQuantity = null;
        if (p.netQuantity() != null) {
            netQuantity = new ProductSummaryResult.NetQuantityDto(
                    p.netQuantity().amount(),
                    p.netQuantity().unit()
            );
        }

        Set<ProductSummaryResult.CategorySummaryResult> categories = Collections.emptySet();
        if (p.categories() != null) {
            categories = p.categories().stream()
                    .map(c -> new ProductSummaryResult.CategorySummaryResult(
                            CategoryId.of(c.id()),
                            c.name()
                    ))
                    .collect(Collectors.toSet());
        }

        return new ProductSummaryResult(
                ProductId.of(p.productId()),
                p.name(),
                p.brand(),
                p.barcode(),
                p.imageUrl(),
                netQuantity,
                categories
        );
    }
}
