package io.github.olszewskif.fridge.catalog.application.service;

import io.github.olszewskif.fridge.catalog.application.dto.*;
import io.github.olszewskif.fridge.catalog.application.mapper.ProductAppMapper;
import io.github.olszewskif.fridge.catalog.application.port.OpenFoodFactsApi;
import io.github.olszewskif.fridge.catalog.application.usecase.ProductUseCase;
import io.github.olszewskif.fridge.catalog.domain.*;
import io.github.olszewskif.fridge.catalog.domain.exception.CategoryNotFoundException;
import io.github.olszewskif.fridge.catalog.domain.exception.ProductNotFoundException;
import io.github.olszewskif.fridge.catalog.domain.port.CategoryRepository;
import io.github.olszewskif.fridge.catalog.domain.port.ProductRepository;
import io.github.olszewskif.fridge.shared.domain.Entity;
import io.github.olszewskif.fridge.shared.domain.PageQuery;
import io.github.olszewskif.fridge.shared.domain.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService implements ProductUseCase {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductAppMapper mapper;
    private final OpenFoodFactsApi openFoodFactsApi;

    @Override
    @Transactional(readOnly = true)
    public ProductResult getProduct(ProductId id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return toResult(product);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<ProductResult> getProducts(
            ProductFilterQuery filters,
            PageQuery pageQuery
    ) {
        PageResult<Product> page = productRepository.findAll(filters, pageQuery);

        // Aggregate and query all categories at once
        Set<CategoryId> categoryIds = page.data().stream()
                .flatMap(p -> p.getCategories().stream())
                .collect(Collectors.toSet());
        Map<CategoryId, Category> categoryMap = categoryRepository.findAllById(categoryIds).stream()
                .collect(Collectors.toMap(Entity::getId, c -> c));

        return page.map(p -> mapper.toResult(p, p.getCategories().stream()
                .map(categoryMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet())));
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public Set<ProductResult> getProducts(ProductFilterQuery filters) {
        return productRepository.findAll(filters).stream()
                .map(this::toResult)
                .collect(Collectors.toSet());
    }

    @Override
    public ProductResult createProduct(CreateProductCommand command) {
        Product product = Product.create(
                command.name(),
                command.brand(),
                command.barcode(),
                command.netQuantity(),
                command.imageUrl(),
                command.categoryIds()
        );
        Set<Category> categories = resolveCategoryIds(product.getCategories());
        return mapper.toResult(productRepository.save(product), categories);
    }

    @Override
    public ProductResult updateProduct(UpdateProductCommand command) {
        Product product = productRepository.findById(command.id())
                .orElseThrow(() -> new ProductNotFoundException(command.id()));
        update(command.name(), product::updateName);
        update(command.brand(), product::updateBrand);
        update(command.barcode(), product::updateBarcode);
        update(command.netQuantity(), product::updateNetQuantity);
        update(command.imageUrl(), product::updateImageUrl);
        return toResult(productRepository.save(product));
    }

    @Override
    public void deleteProduct(ProductId id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.delete(id);
    }

    @Override
    public ProductResult addCategory(AddCategoryCommand command) {
        Product product = productRepository.findById(command.productId())
                .orElseThrow(() -> new ProductNotFoundException(command.productId()));
        if (!categoryRepository.existsById(command.categoryId())) {
            throw new CategoryNotFoundException(command.categoryId());
        }

        product.addCategory(command.categoryId());
        return toResult(productRepository.save(product));
    }

    @Override
    public ProductResult removeCategory(RemoveCategoryCommand command) {
        Product product = productRepository.findById(command.productId())
                .orElseThrow(() -> new ProductNotFoundException(command.productId()));
        if (!categoryRepository.existsById(command.categoryId())) {
            throw new CategoryNotFoundException(command.categoryId());
        }

        product.removeCategory(command.categoryId());
        return toResult(productRepository.save(product));
    }

    @Override
    public ProductResult getProductByBarcode(Barcode barcode) {
        Optional<Product> product = productRepository.findByBarcode(barcode);

        if (product.isPresent()) {
            return toResult(product.get());
        }

        ExternalProduct externalProduct = openFoodFactsApi.findByBarcode(barcode)
                .orElseThrow(() -> new ProductNotFoundException(barcode));

        Set<String> categoryNames = externalProduct.categories();
        Set<Category> categories = resolveCategoryNames(categoryNames);

        Product newProduct = Product.create(
                externalProduct.name(),
                externalProduct.brand(),
                barcode,
                externalProduct.netQuantity(),
                externalProduct.imageUrl(),
                categories.stream().map(Entity::getId).collect(Collectors.toSet())
        );
        productRepository.save(newProduct);
        return mapper.toResult(newProduct, categories);
    }

    private ProductResult toResult(Product product) {
        return mapper.toResult(product, categoryRepository.findAllById(product.getCategories()));
    }

    private Set<Category> resolveCategoryIds(Set<CategoryId> ids) {
        Set<Category> categories = categoryRepository.findAllById(ids);
        Set<CategoryId> found = categories.stream()
                .map(Entity::getId)
                .collect(Collectors.toSet());
        for (CategoryId id : ids) {
            if (!found.contains(id)) {
                throw new CategoryNotFoundException(id);
            }
        }
        return categories;
    }

    private Set<Category> resolveCategoryNames(Set<String> names) {
        return categoryRepository.findAllByNames(names);
    }

    private static <T> void update(T field, Consumer<T> consumer) {
        if (field != null) {
            consumer.accept(field);
        }
    }
}
