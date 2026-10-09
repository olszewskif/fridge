package io.github.olszewskif.fridge.catalog.application.usecase;

import io.github.olszewskif.fridge.catalog.application.dto.*;
import io.github.olszewskif.fridge.catalog.domain.ProductId;
import io.github.olszewskif.fridge.shared.domain.PageResult;
import io.github.olszewskif.fridge.shared.domain.PageQuery;

public interface ProductUseCase {
    ProductResult getProduct(ProductId id);
    PageResult<ProductResult> getProducts(PageQuery pageQuery);
    ProductResult createProduct(CreateProductCommand command);
    ProductResult updateProduct(UpdateProductCommand command);
    void deleteProduct(ProductId id);
    ProductResult addCategory(AddCategoryCommand command);
    ProductResult removeCategory(RemoveCategoryCommand command);
}
