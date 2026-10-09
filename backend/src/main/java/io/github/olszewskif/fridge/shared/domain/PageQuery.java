package io.github.olszewskif.fridge.shared.domain;

import java.util.List;

public record PageQuery(
        int page,
        int size,
        List<Sort> sort
) {

    public PageQuery {
        if (page < 0) {
            throw new InvalidPageSizeException();
        }
        sort = sort == null ? List.of() : List.copyOf(sort);
    }

    public record Sort(
            String field,
            Direction direction
    ) {

        public enum Direction {
            ASC, DESC
        }
    }
}
