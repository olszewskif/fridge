package io.github.olszewskif.fridge.inventory.domain;

import io.github.olszewskif.fridge.inventory.domain.exception.InvalidUnitException;
import lombok.Getter;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public enum Unit {
    MILLILITERS ("milliliters"),
    LITERS      ("liters"),
    GRAMS       ("grams"),
    KILOGRAMS   ("kilograms"),
    PIECES      ("pieces");

    @Getter
    private final String name;
    private static final Map<String, Unit> VALUES = Arrays.stream(Unit.values())
            .collect(Collectors.toMap(v -> v.name, v -> v));

    Unit(String name) {
        this.name = name;
    }

    public static Unit fromString(String name) {
        Unit unit = VALUES.get(name.toLowerCase(Locale.ROOT));
        if (unit == null) {
            throw new InvalidUnitException("Unknown unit: '" + name + "'");
        }
        return unit;
    }
}
