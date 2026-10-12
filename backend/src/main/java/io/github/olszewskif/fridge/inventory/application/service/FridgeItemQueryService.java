package io.github.olszewskif.fridge.inventory.application.service;

import io.github.olszewskif.fridge.inventory.application.dto.*;
import io.github.olszewskif.fridge.inventory.application.port.FridgeItemQueryRepository;
import io.github.olszewskif.fridge.inventory.application.port.ProductResolver;
import io.github.olszewskif.fridge.inventory.application.usecase.FridgeItemQueryUseCase;
import io.github.olszewskif.fridge.inventory.domain.*;
import io.github.olszewskif.fridge.inventory.domain.exception.FridgeItemNotFoundException;
import io.github.olszewskif.fridge.shared.domain.PageQuery;
import io.github.olszewskif.fridge.shared.domain.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FridgeItemQueryService implements FridgeItemQueryUseCase {

    private final FridgeItemQueryRepository fridgeItemQueryRepository;
    private final ProductResolver productResolver;

    @Override
    public FridgeItemResult getItem(GetFridgeItemQuery query) {
        FridgeItemResult item = fridgeItemQueryRepository.findById(query)
                .orElseThrow(() -> new FridgeItemNotFoundException(query.fridgeItemId()));

        ResolveProductResult productResult = productResolver.resolveProduct(
                new ResolveProductQuery(item.productId()));

        return item.withProduct(productResult != null ? productResult.product() : null);
    }

    @Override
    public PageResult<FridgeItemResult> getItems(FridgeItemsFilterQuery filters,
                                                 PageQuery pageQuery) {
        Set<ProductId> allProducts = null;

        if ((filters.search() != null && !filters.search().isBlank()) ||
            (filters.categoryIds() != null && !filters.categoryIds().isEmpty())) {
            var command = new SearchProductsQuery(filters.search(), filters.categoryIds());
            SearchProductsResult res = productResolver.searchProducts(command);

            allProducts = res.products().stream().map(ProductSummaryResult::productId)
                    .collect(Collectors.toSet());

            if (allProducts.isEmpty()) {
                return new PageResult<>(List.of(), pageQuery.page(), pageQuery.size(), 0);
            }
        }

        var filterQuery = new ResolvedFridgeItemsFilterQuery(
                filters.fridgeId(),
                filters.isExpired(),
                filters.expiresInDays(),
                filters.addedBy(),
                filters.productId(),
                allProducts);

        PageResult<FridgeItemResult> pageResult = fridgeItemQueryRepository.findAll(filterQuery, pageQuery);
        if (pageResult.data().isEmpty()) {
            return pageResult;
        }

        Set<ProductId> productIds = pageResult.data().stream()
                .map(FridgeItemResult::productId)
                .collect(Collectors.toSet());

        ResolveProductsBatchResult batchResult = productResolver.resolveProductsBatch(
                new ResolveProductsBatchQuery(productIds)
        );
        Map<ProductId, ProductSummaryResult> productMap = batchResult.asMap();

        return pageResult.map(item -> item.withProduct(
                productMap.get(item.productId())));
    }

}
