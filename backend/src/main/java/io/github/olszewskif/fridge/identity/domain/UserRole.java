package io.github.olszewskif.fridge.identity.domain;

import io.github.olszewskif.fridge.shared.domain.DomainUtil;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum UserRole {
    USER    ("ROLE_USER"),
    ADMIN   ("ROLE_ADMIN");

    private static final Map<String, UserRole> VALUES = Arrays.stream(UserRole.values())
            .collect(Collectors.toMap(u -> u.getName(), u -> u));

    @Getter private final String name;

    UserRole(String name) {
        this.name = name;
    }

    public static UserRole fromName(String name) {
        DomainUtil.notNull(name, "Name cannot be null!");
        return VALUES.getOrDefault(name, USER);
    }
}
