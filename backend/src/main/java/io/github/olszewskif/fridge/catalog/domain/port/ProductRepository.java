package io.github.olszewskif.fridge.catalog.domain.port;

import io.github.olszewskif.fridge.catalog.domain.Barcode;
import io.github.olszewskif.fridge.catalog.domain.Product;
import io.github.olszewskif.fridge.catalog.domain.ProductFilterQuery;
import io.github.olszewskif.fridge.catalog.domain.ProductId;
import io.github.olszewskif.fridge.shared.domain.PageQuery;
import io.github.olszewskif.fridge.shared.domain.PageResult;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductRepository {
    Optional<Product> findById(ProductId id);
    boolean existsById(ProductId id);
    PageResult<Product> findAll(ProductFilterQuery filters, PageQuery pageQuery);
    Set<Product> findAll(ProductFilterQuery filters);
    Product save(Product product);
    void delete(ProductId id);
    Optional<Product> findByBarcode(Barcode barcode);
}
