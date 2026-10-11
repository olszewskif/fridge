package io.github.olszewskif.fridge.inventory.adapter.outbound.internal;

import io.github.olszewskif.fridge.catalog.adapter.inbound.internal.ProductInternalApi;
import io.github.olszewskif.fridge.catalog.adapter.inbound.internal.dto.ResolveProductsRequest;
import io.github.olszewskif.fridge.catalog.adapter.inbound.internal.dto.ResolveProductsResponse;
import io.github.olszewskif.fridge.inventory.application.dto.ResolveProductsCommand;
import io.github.olszewskif.fridge.inventory.application.dto.ResolveProductsResult;
import io.github.olszewskif.fridge.inventory.application.port.ProductResolver;
import io.github.olszewskif.fridge.inventory.domain.ProductId;
import io.github.olszewskif.fridge.shared.domain.DomainId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
class ProductResolverAdapter implements ProductResolver {

    private final ProductInternalApi productInternalApi;

    @Override
    public ResolveProductsResult resolveProducts(ResolveProductsCommand command) {
        Set<UUID> uuids = command.categoryIds() == null ? null
                : command.categoryIds().stream()
                        .map(DomainId::getValue)
                        .collect(Collectors.toSet());
        var request = new ResolveProductsRequest(command.search(), uuids);

        ResolveProductsResponse response = productInternalApi.resolveProducts(request);

        log.info("{}", response);

        return new ResolveProductsResult(response.products().stream().map(p ->
                new ResolveProductsResult.ProductSummaryResult(
                        ProductId.of(p.productId()),
                        p.name(),
                        p.brand()))
                .collect(Collectors.toSet()));
    }
}
