package io.github.olszewskif.fridge.catalog.application.mapper;

import io.github.olszewskif.fridge.catalog.application.dto.CategoryResult;
import io.github.olszewskif.fridge.catalog.domain.Alias;
import io.github.olszewskif.fridge.catalog.domain.Category;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public final class CategoryAppMapper {

    public CategoryResult toResult(Category category) {
        Set<String> aliases = category.getAliases() == null ? Collections.emptySet()
                : category.getAliases().stream().map(Alias::value).collect(Collectors.toSet());

        return new CategoryResult(
                category.getId(),
                category.getName(),
                category.getParentId(),
                aliases
        );
    }
}
