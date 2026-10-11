package io.github.olszewskif.fridge.catalog.application.mapper;

import io.github.olszewskif.fridge.catalog.application.dto.ProductResult;
import io.github.olszewskif.fridge.catalog.domain.Category;
import io.github.olszewskif.fridge.catalog.domain.Product;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public final class ProductAppMapper {

    public ProductResult toResult(Product product, Set<Category> categories) {
        return new ProductResult(
                product.getId(),
                product.getName(),
                product.getBrand(),
                product.getBarcode(),
                product.getNetQuantity(),
                product.getImageUrl(),
                mapToSummary(categories)
        );
    }

    private static Set<ProductResult.CategorySummary> mapToSummary(Set<Category> categories) {
        return categories.stream().map(c -> new ProductResult.CategorySummary(
                c.getId(), c.getName(), c.getParentId()
        )).collect(Collectors.toSet());
    }
}
