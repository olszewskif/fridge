package io.github.olszewskif.fridge.shared.adapter.inbound.web.mapper;

import io.github.olszewskif.fridge.shared.domain.PageQuery;
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
}
