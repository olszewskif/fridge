package io.github.olszewskif.fridge.shared.adapter.inbound.web.mapper;

import io.github.olszewskif.fridge.shared.domain.PageQuery;
import io.github.olszewskif.fridge.shared.domain.PageResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class CommonWebMapper {

    public PageQuery toPageQuery(Pageable pageable) {
        List<PageQuery.Sort> sort = pageable.getSort().stream()
                .map(order -> new PageQuery.Sort(
                        order.getProperty(),
                        order.isAscending() ? PageQuery.Sort.Direction.ASC : PageQuery.Sort.Direction.DESC
                ))
                .toList();
        return new PageQuery(pageable.getPageNumber(), pageable.getPageSize(), sort);
    }

    public <T> Page<T> toPage(PageResult<T> result, Pageable pageable) {
        return new PageImpl<>(result.data(), pageable, result.totalElements());
    }
}
