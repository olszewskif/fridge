package io.github.olszewskif.fridge.catalog.adapter.inbound.internal;

import io.github.olszewskif.fridge.catalog.adapter.inbound.internal.dto.ResolveProductsRequest;
import io.github.olszewskif.fridge.catalog.adapter.inbound.internal.dto.ResolveProductsResponse;
import io.github.olszewskif.fridge.catalog.application.dto.ProductResult;
import io.github.olszewskif.fridge.catalog.application.usecase.ProductUseCase;
import io.github.olszewskif.fridge.catalog.domain.CategoryId;
import io.github.olszewskif.fridge.catalog.domain.Product;
import io.github.olszewskif.fridge.catalog.domain.ProductFilterQuery;
import io.github.olszewskif.fridge.shared.domain.PageQuery;
import io.github.olszewskif.fridge.shared.domain.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
class ProductModuleFacade implements ProductInternalApi {

    private final ProductUseCase productUseCase;

    @Override
    public ResolveProductsResponse resolveProducts(ResolveProductsRequest request) {
        Set<CategoryId> categoryIds = request.categoryIds() == null ? null
                : request.categoryIds().stream()
                .map(CategoryId::of)
                .collect(Collectors.toSet());
        var filters = new ProductFilterQuery(request.search(), categoryIds);

        Set<ProductResult> products = productUseCase.getProducts(filters);

        log.info("{}", products);

        return new ResolveProductsResponse(products.stream().map(p ->
                new ResolveProductsResponse.ProductSummary(
                        p.id().getValue(), p.name(), p.brand()))
                .collect(Collectors.toSet()));
    }

}
