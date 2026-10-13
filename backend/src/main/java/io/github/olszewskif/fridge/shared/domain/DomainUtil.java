package io.github.olszewskif.fridge.shared.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DomainUtil {

    public static String notBlank(String string, String message) {
        if (string == null || string.isBlank()) {
            throw new DomainException(message);
        }
        return string;
    }

    public static <T> T notNull(T obj, String message) {
        if (obj == null) {
            throw new DomainException(message);
        }
        return obj;
    }

    public static <T extends Collection<?>> T notEmpty(T collection, String message) {
        if (collection == null || collection.isEmpty()) {
            throw new DomainException(message);
        }
        return collection;
    }
}
