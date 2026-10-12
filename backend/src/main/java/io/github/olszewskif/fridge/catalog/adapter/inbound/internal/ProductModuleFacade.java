package io.github.olszewskif.fridge.catalog.adapter.inbound.internal;

import io.github.olszewskif.fridge.catalog.adapter.inbound.internal.dto.*;
import io.github.olszewskif.fridge.catalog.application.dto.ProductResult;
import io.github.olszewskif.fridge.catalog.application.usecase.ProductUseCase;
import io.github.olszewskif.fridge.catalog.domain.CategoryId;
import io.github.olszewskif.fridge.catalog.domain.NetQuantity;
import io.github.olszewskif.fridge.catalog.domain.ProductFilterQuery;
import io.github.olszewskif.fridge.catalog.domain.ProductId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
class ProductModuleFacade implements ProductInternalApi {

    private final ProductUseCase productUseCase;

    @Override
    public SearchProductsResponse searchProducts(SearchProductsRequest request) {
        if ((request.search() == null || request.search().isBlank()) &&
            (request.categoryIds() == null || request.categoryIds().isEmpty())) {
            return new SearchProductsResponse(Collections.emptySet());
        }

        Set<CategoryId> categoryIds = request.categoryIds() == null ? null
                : request.categoryIds().stream()
                .map(CategoryId::of)
                .collect(Collectors.toSet());
        var filters = new ProductFilterQuery(request.search(), categoryIds);

        Set<ProductResult> products = productUseCase.getProducts(filters);

        return new SearchProductsResponse(products.stream()
                .map(this::toProductSummary)
                .collect(Collectors.toSet()));
    }

    @Override
    public ResolveProductResponse resolveProduct(ResolveProductRequest request) {
        if (request.productId() == null) {
            return new ResolveProductResponse(null);
        }
        ProductResult product = productUseCase.getProduct(ProductId.of(request.productId()));
        return new ResolveProductResponse(toProductSummary(product));
    }

    @Override
    public ResolveProductsBatchResponse resolveProductsBatch(
            ResolveProductsBatchRequest request) {
        if (request.productIds() == null || request.productIds().isEmpty()) {
            return new ResolveProductsBatchResponse(Collections.emptySet());
        }
        Set<ProductId> productIds = request.productIds().stream()
                .map(ProductId::of)
                .collect(Collectors.toSet());

        Set<ProductResult> products = productUseCase.getProductsByIds(productIds);
        Set<ProductSummary> summaries = products.stream()
                .map(this::toProductSummary)
                .collect(Collectors.toSet());

        return new ResolveProductsBatchResponse(summaries);
    }

    private ProductSummary toProductSummary(ProductResult product) {
        NetQuantity netQuantity = product.netQuantity();
        ProductSummary.NetQuantityDto netQuantityDto = netQuantity == null ? null
                : new ProductSummary.NetQuantityDto(
                        netQuantity.amount(),
                        netQuantity.unit().getDisplayName()
                );

        Set<ProductSummary.CategorySummary> categorySummaries = product.categories() == null ? Collections.emptySet()
                : product.categories().stream()
                .map(c -> new ProductSummary.CategorySummary(c.id().getValue(), c.name()))
                .collect(Collectors.toSet());

        return new ProductSummary(
                product.id().getValue(),
                product.name(),
                product.brand(),
                product.barcode().value(),
                product.imageUrl(),
                netQuantityDto,
                categorySummaries
        );
    }

}
