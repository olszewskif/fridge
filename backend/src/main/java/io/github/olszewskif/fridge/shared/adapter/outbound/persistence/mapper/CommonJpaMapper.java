package io.github.olszewskif.fridge.shared.adapter.outbound.persistence.mapper;

import io.github.olszewskif.fridge.shared.domain.PageQuery;
import io.github.olszewskif.fridge.shared.domain.PageResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public final class CommonJpaMapper {

    public Pageable toPageable(PageQuery pageQuery) {
        Sort sort = Sort.by(pageQuery.sort().stream()
                .map(CommonJpaMapper::toSpringSortOrder)
                .toList());
        return PageRequest.of(pageQuery.page(), pageQuery.size(), sort);
    }

    public <T> PageResult<T> toPageResult(Page<T> page) {
        return new PageResult<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements()
        );
    }

    private static Sort.Order toSpringSortOrder(PageQuery.Sort sort) {
        return new Sort.Order(sort.direction() == PageQuery.Sort.Direction.ASC
                ? Sort.Direction.ASC
                : Sort.Direction.DESC,
                sort.field()
        );
    }
}
