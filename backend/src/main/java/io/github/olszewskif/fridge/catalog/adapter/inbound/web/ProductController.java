package io.github.olszewskif.fridge.catalog.adapter.inbound.web;

import io.github.olszewskif.fridge.catalog.adapter.inbound.web.dto.*;
import io.github.olszewskif.fridge.catalog.adapter.inbound.web.mapper.ProductWebMapper;
import io.github.olszewskif.fridge.catalog.application.dto.AddCategoryCommand;
import io.github.olszewskif.fridge.catalog.application.dto.RemoveCategoryCommand;
import io.github.olszewskif.fridge.catalog.application.usecase.ProductUseCase;
import io.github.olszewskif.fridge.catalog.domain.Barcode;
import io.github.olszewskif.fridge.catalog.domain.CategoryId;
import io.github.olszewskif.fridge.catalog.domain.ProductFilterQuery;
import io.github.olszewskif.fridge.catalog.domain.ProductId;
import io.github.olszewskif.fridge.shared.adapter.inbound.web.mapper.CommonWebMapper;
import io.github.olszewskif.fridge.shared.domain.PageQuery;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductUseCase useCase;
    private final ProductWebMapper mapper;
    private final CommonWebMapper commonWebMapper;

    @GetMapping("/{id}")
    public ProductResponse getProduct(@PathVariable UUID id) {
        return mapper.toResponse(useCase.getProduct(ProductId.of(id)));
    }

    @GetMapping
    public Page<ProductResponse> getProducts(
            ProductFilterRequest request,
            Pageable pageable
    ) {
        PageQuery pageQuery = commonWebMapper.toPageQuery(pageable);
        var filters = mapper.toFilterQuery(request);
        var result = useCase.getProducts(filters, pageQuery).map(mapper::toResponse);
        return new PageImpl<>(result.data(), pageable, result.totalElements());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@Valid @RequestBody CreateProductRequest request) {
        return mapper.toResponse(useCase.createProduct(mapper.toCommand(request)));
    }

    @PatchMapping("/{id}")
    public ProductResponse updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateProductRequest request
    ) {
        return mapper.toResponse(useCase.updateProduct(mapper.toCommand(id, request)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable UUID id) {
        useCase.deleteProduct(ProductId.of(id));
    }

    @PutMapping("/{id}/categories/{categoryId}")
    public ProductResponse addCategory(
            @PathVariable UUID id,
            @PathVariable UUID categoryId
    ) {
        var command = new AddCategoryCommand(ProductId.of(id), CategoryId.of(categoryId));
        return mapper.toResponse(useCase.addCategory(command));
    }

    @DeleteMapping("/{id}/categories/{categoryId}")
    public ProductResponse removeCategory(
            @PathVariable UUID id,
            @PathVariable UUID categoryId
    ) {
        var command = new RemoveCategoryCommand(ProductId.of(id), CategoryId.of(categoryId));
        return mapper.toResponse(useCase.removeCategory(command));
    }

    @PostMapping("/scan/{barcode}")
    public ProductResponse scanProduct(@PathVariable String barcode) {
        return mapper.toResponse(useCase.getProductByBarcode(new Barcode(barcode)));
    }

}
