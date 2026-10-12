package io.github.olszewskif.fridge.catalog.application.usecase;

import io.github.olszewskif.fridge.catalog.application.dto.*;
import io.github.olszewskif.fridge.catalog.domain.Barcode;
import io.github.olszewskif.fridge.catalog.domain.ProductFilterQuery;
import io.github.olszewskif.fridge.catalog.domain.ProductId;
import io.github.olszewskif.fridge.shared.domain.PageQuery;
import io.github.olszewskif.fridge.shared.domain.PageResult;

import java.util.Set;

public interface ProductUseCase {
    ProductResult getProduct(ProductId id);
    PageResult<ProductResult> getProducts(ProductFilterQuery filters, PageQuery pageQuery);
    Set<ProductResult> getProducts(ProductFilterQuery filters);
    Set<ProductResult> getProductsByIds(Set<ProductId> productIds);
    ProductResult createProduct(CreateProductCommand command);
    ProductResult updateProduct(UpdateProductCommand command);
    void deleteProduct(ProductId id);
    ProductResult addCategory(AddCategoryCommand command);
    ProductResult removeCategory(RemoveCategoryCommand command);
    ProductResult getProductByBarcode(Barcode barcode);
}
