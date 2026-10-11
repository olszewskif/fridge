package io.github.olszewskif.fridge.catalog.domain;

import io.github.olszewskif.fridge.catalog.domain.exception.InvalidUnitException;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

public enum Unit {
    MILLILITERS ("milliliters", List.of("milliliter", "ml", "millilitres", "millilitre")),
    LITERS      ("liters",      List.of("liter", "l", "litre", "litres")),
    GRAMS       ("grams",       List.of("gram", "g")),
    KILOGRAMS   ("kilograms",   List.of("kilogram", "kg", "kilo")),
    PIECES      ("pieces",      List.of("piece", "pcs", "pc"));

    @Getter
    private final String displayName;
    private final List<String> aliases;
    private static final Map<String, Unit> VALUES = new HashMap<>();

    static {
        for (Unit unit : Unit.values()) {
            VALUES.put(unit.displayName.toLowerCase(Locale.ROOT), unit);
            VALUES.put(unit.name().toLowerCase(Locale.ROOT), unit);
            for (String alias : unit.aliases) {
                VALUES.put(alias.toLowerCase(Locale.ROOT), unit);
            }
        }
    }

    Unit(String displayName, List<String> aliases) {
        this.displayName = displayName;
        this.aliases = aliases;
    }

    public static Unit fromString(String name) {
        return find(name).orElseThrow(() -> new InvalidUnitException("Unknown unit: '" + name + "'"));
    }

    public static Optional<Unit> find(String name) {
        if (name == null || name.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUES.get(name.trim().toLowerCase(Locale.ROOT)));
    }
}
