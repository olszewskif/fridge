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
        return fridgeItemQueryRepository.findById(query)
                .orElseThrow(() -> new FridgeItemNotFoundException(query.fridgeItemId()));
    }

    @Override
    public PageResult<FridgeItemResult> getItems(FridgeItemsFilterQuery filters,
                                                 PageQuery pageQuery) {
        Set<ProductId> allProducts = null;

        if ((filters.search() != null && !filters.search().isBlank()) ||
            (filters.categoryIds() != null && !filters.categoryIds().isEmpty())) {
            var command = new ResolveProductsCommand(filters.search(), filters.categoryIds());
            ResolveProductsResult res = productResolver.resolveProducts(command);

            allProducts = res.products().stream().map(p -> p.productId())
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
        return fridgeItemQueryRepository.findAll(filterQuery, pageQuery);
    }

}
