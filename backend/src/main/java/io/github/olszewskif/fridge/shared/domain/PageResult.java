package io.github.olszewskif.fridge.shared.domain;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public record PageResult<T>(
    List<T> data,
    int page,
    int size,
    long totalElements
) {

    public <U> PageResult<U> map(Function<T, U> mapper) {
        Objects.requireNonNull(mapper);
        return new PageResult<>(
                data.stream().map(mapper).toList(),
                page,
                size,
                totalElements
        );
    }
}
