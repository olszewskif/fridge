package io.github.olszewskif.fridge.identity.domain;

import io.github.olszewskif.fridge.identity.domain.exception.InvalidEmailException;
import io.github.olszewskif.fridge.shared.domain.DomainUtil;

import java.util.regex.Pattern;

public record Email(String value) {

    private static final Pattern PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    public Email {
        DomainUtil.notBlank(value, "Email cannot be null or blank");
        if (!PATTERN.matcher(value).matches()) {
            throw new InvalidEmailException(value);
        }
    }
}
