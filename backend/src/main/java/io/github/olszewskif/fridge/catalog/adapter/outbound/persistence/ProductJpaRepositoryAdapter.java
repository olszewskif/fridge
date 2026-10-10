package io.github.olszewskif.fridge.catalog.adapter.outbound.persistence;

import io.github.olszewskif.fridge.catalog.adapter.outbound.persistence.mapper.ProductJpaMapper;
import io.github.olszewskif.fridge.catalog.adapter.outbound.persistence.specification.ProductSpecifications;
import io.github.olszewskif.fridge.catalog.domain.Barcode;
import io.github.olszewskif.fridge.catalog.domain.Product;
import io.github.olszewskif.fridge.catalog.domain.ProductFilterQuery;
import io.github.olszewskif.fridge.catalog.domain.ProductId;
import io.github.olszewskif.fridge.catalog.domain.port.ProductRepository;
import io.github.olszewskif.fridge.shared.adapter.outbound.persistence.mapper.CommonJpaMapper;
import io.github.olszewskif.fridge.shared.domain.PageQuery;
import io.github.olszewskif.fridge.shared.domain.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductJpaRepositoryAdapter implements ProductRepository {

    private final ProductJpaRepository jpaRepository;
    private final ProductJpaMapper mapper;
    private final CommonJpaMapper commonJpaMapper;
    private final ProductSpecifications specifications;

    @Override
    public Optional<Product> findById(ProductId id) {
        return jpaRepository.findById(id.getValue()).map(mapper::toDomainEntity);
    }

    @Override
    public boolean existsById(ProductId id) {
        return jpaRepository.existsById(id.getValue());
    }

    @Override
    public PageResult<Product> findAll(ProductFilterQuery filters, PageQuery pageQuery) {
        Pageable pageable = commonJpaMapper.toPageable(pageQuery);
        Specification<ProductJpaEntity> spec = specifications.build(filters);
        Page<ProductJpaEntity> page = jpaRepository.findAll(spec, pageable);
        return commonJpaMapper.toPageResult(page)
                .map(mapper::toDomainEntity);
    }

    @Override
    public Product save(Product product) {
        ProductJpaEntity entity = jpaRepository.findById(product.getId().getValue())
                .map(existing -> {
                    mapper.updateJpaEntity(product, existing);
                    return existing;
                })
                .orElseGet(() -> mapper.toJpaEntity(product));
        return mapper.toDomainEntity(jpaRepository.save(entity));
    }

    @Override
    public void delete(ProductId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    public Optional<Product> findByBarcode(Barcode barcode) {
        return jpaRepository.findByBarcode(barcode.value()).map(mapper::toDomainEntity);
    }

}
