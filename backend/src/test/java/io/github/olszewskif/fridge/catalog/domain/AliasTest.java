package io.github.olszewskif.fridge.catalog.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AliasTest {

    @Test
    void shouldCreateValidAliasWithTrimAndLowercase() {
        Alias alias = Alias.of("  Dairy Products  ");
        assertEquals("dairy products", alias.value());
    }

    @Test
    void shouldThrowExceptionWhenNull() {
        assertThrows(IllegalArgumentException.class, () -> Alias.of(null));
    }

    @Test
    void shouldThrowExceptionWhenBlank() {
        assertThrows(IllegalArgumentException.class, () -> Alias.of("   "));
    }

    @Test
    void shouldBeEqualForSameNormalizedValue() {
        Alias alias1 = Alias.of("MILK");
        Alias alias2 = Alias.of("milk");
        assertEquals(alias1, alias2);
        assertEquals(alias1.hashCode(), alias2.hashCode());
    }
}
