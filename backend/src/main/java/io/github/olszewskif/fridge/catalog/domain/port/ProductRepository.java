package io.github.olszewskif.fridge.catalog.domain.port;

import io.github.olszewskif.fridge.catalog.domain.Product;
import io.github.olszewskif.fridge.catalog.domain.ProductId;
import io.github.olszewskif.fridge.shared.domain.PageQuery;
import io.github.olszewskif.fridge.shared.domain.PageResult;

import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(ProductId id);
    boolean existsById(ProductId id);
    PageResult<Product> findAll(PageQuery pageQuery);
    Product save(Product product);
    void delete(ProductId id);
}
